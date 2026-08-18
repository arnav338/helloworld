#!/usr/bin/env python3
"""Build full-resolution diagrams and a polished PDF from the project plan."""

from __future__ import annotations

import html
import re
from pathlib import Path

from PIL import Image as PILImage, ImageDraw, ImageFont
from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER, TA_LEFT
from reportlab.lib.pagesizes import LETTER
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import inch
from reportlab.platypus import (
    Image,
    LongTable,
    PageBreak,
    Paragraph,
    Preformatted,
    SimpleDocTemplate,
    Spacer,
    TableStyle,
)


ROOT = Path(__file__).resolve().parent
PLAN_MD = ROOT / "Mini-RAG-Project-Plan.md"
OUTPUT_PDF = ROOT / "Mini-RAG-Project-Plan.pdf"
DIAGRAM_DIR = ROOT / "diagrams"

NAVY = "#12263A"
BLUE = "#2563EB"
SKY = "#E8F1FF"
TEAL = "#0F766E"
TEAL_BG = "#E4F7F4"
AMBER = "#B45309"
AMBER_BG = "#FFF4DD"
SLATE = "#475569"
LIGHT = "#F6F8FB"
WHITE = "#FFFFFF"
LINE = "#CBD5E1"


def font(size: int, bold: bool = False) -> ImageFont.FreeTypeFont:
    candidates = [
        Path("/System/Library/Fonts/Supplemental/Arial Bold.ttf" if bold else "/System/Library/Fonts/Supplemental/Arial.ttf"),
        Path("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf" if bold else "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf"),
    ]
    for candidate in candidates:
        if candidate.exists():
            return ImageFont.truetype(str(candidate), size)
    return ImageFont.load_default()


def centered_multiline(draw: ImageDraw.ImageDraw, box, text: str, size=34, color=NAVY, bold=False, spacing=10):
    x1, y1, x2, y2 = box
    f = font(size, bold)
    bbox = draw.multiline_textbbox((0, 0), text, font=f, spacing=spacing, align="center")
    width = bbox[2] - bbox[0]
    height = bbox[3] - bbox[1]
    draw.multiline_text(((x1 + x2 - width) / 2, (y1 + y2 - height) / 2), text, font=f, fill=color, spacing=spacing, align="center")


def card(draw, box, title, subtitle="", fill=WHITE, outline=LINE, title_color=NAVY):
    draw.rounded_rectangle(box, radius=24, fill=fill, outline=outline, width=4)
    x1, y1, x2, y2 = box
    if subtitle:
        centered_multiline(draw, (x1 + 25, y1 + 20, x2 - 25, y1 + 105), title, 31, title_color, True)
        subtitle_color = "#D7E4F2" if fill == NAVY else SLATE
        centered_multiline(draw, (x1 + 30, y1 + 100, x2 - 30, y2 - 18), subtitle, 23, subtitle_color, False, 8)
    else:
        centered_multiline(draw, (x1 + 20, y1 + 15, x2 - 20, y2 - 15), title, 30, title_color, True)


def arrow(draw, start, end, color=BLUE, width=8):
    draw.line([start, end], fill=color, width=width)
    x2, y2 = end
    x1, y1 = start
    dx, dy = x2 - x1, y2 - y1
    length = max((dx * dx + dy * dy) ** 0.5, 1)
    ux, uy = dx / length, dy / length
    px, py = -uy, ux
    head = 22
    wing = 13
    p1 = (x2, y2)
    p2 = (x2 - ux * head + px * wing, y2 - uy * head + py * wing)
    p3 = (x2 - ux * head - px * wing, y2 - uy * head - py * wing)
    draw.polygon([p1, p2, p3], fill=color)


def canvas(title: str, subtitle: str):
    img = PILImage.new("RGB", (2200, 1300), WHITE)
    draw = ImageDraw.Draw(img)
    draw.rectangle((0, 0, 2200, 160), fill=NAVY)
    draw.text((90, 42), title, font=font(52, True), fill=WHITE)
    draw.text((92, 106), subtitle, font=font(25), fill="#D7E4F2")
    return img, draw


def save_diagram(img: PILImage.Image, stem: str):
    png = DIAGRAM_DIR / f"{stem}.png"
    img.save(png, "PNG", dpi=(180, 180))
    return png


def build_diagrams():
    DIAGRAM_DIR.mkdir(parents=True, exist_ok=True)

    # Diagram 1: complete indexing and query paths.
    img, draw = canvas("Mini RAG system architecture", "Two controlled paths: index documents once, retrieve evidence for every question")
    draw.text((90, 205), "INDEXING PATH", font=font(29, True), fill=TEAL)
    top_boxes = [(90 + i * 410, 260, 410 + i * 410, 470) for i in range(5)]
    top = [
        ("PDF upload", "Validate type, size\nand checksum"),
        ("PDFBox", "Extract text and\npage metadata"),
        ("Chunking", "Paragraph-aware\nwith overlap"),
        ("Embedding model", "Batch chunks into\nnumeric vectors"),
        ("SQLite", "Persist documents,\nchunks and vectors"),
    ]
    for box, (title, sub) in zip(top_boxes, top):
        card(draw, box, title, sub, TEAL_BG, "#74CFC2", TEAL)
    for a, b in zip(top_boxes, top_boxes[1:]):
        arrow(draw, (a[2] + 8, (a[1] + a[3]) // 2), (b[0] - 8, (b[1] + b[3]) // 2), TEAL)

    draw.text((90, 580), "QUESTION PATH", font=font(29, True), fill=BLUE)
    bottom_boxes = [(90 + i * 350, 635, 360 + i * 350, 850) for i in range(6)]
    bottom = [
        ("Question", "User asks in\nnatural language"),
        ("Query embedding", "Use the same\nembedding model"),
        ("Java retrieval", "Cosine similarity,\nthreshold and top-k"),
        ("Context builder", "Bound and label\nretrieved evidence"),
        ("Chat model", "Answer only from\nthe evidence"),
        ("Response", "Answer, citations,\nscores and metadata"),
    ]
    for box, (title, sub) in zip(bottom_boxes, bottom):
        card(draw, box, title, sub, SKY, "#98BDF8", BLUE)
    for a, b in zip(bottom_boxes, bottom_boxes[1:]):
        arrow(draw, (a[2] + 6, (a[1] + a[3]) // 2), (b[0] - 6, (b[1] + b[3]) // 2), BLUE)
    arrow(draw, (top_boxes[-1][2] - 100, top_boxes[-1][3] + 8), (bottom_boxes[2][2] - 80, bottom_boxes[2][1] - 8), AMBER)
    centered_multiline(draw, (1540, 475, 2050, 620), "Stored vectors and metadata\nare candidates for retrieval", 23, AMBER, True)
    draw.rounded_rectangle((90, 1010, 2110, 1190), radius=24, fill=LIGHT, outline=LINE, width=3)
    centered_multiline(draw, (130, 1020, 2070, 1180), "Control boundary: the Java application owns files, database access, ranking and prompt construction.\nThe chat model receives only the question and selected evidence - never direct database or filesystem access.", 29, NAVY, True, 14)
    save_diagram(img, "01-system-architecture")

    # Diagram 2: pluggable providers and stores.
    img, draw = canvas("Provider-neutral design", "Chat, embeddings and storage vary independently behind small Java interfaces")
    card(draw, (730, 235, 1470, 420), "RAG application core", "Chunking, retrieval, context construction,\ngrounding, citations and evaluation", NAVY, NAVY, WHITE)
    branches = [
        ((110, 610, 680, 805), "ChatModel", "Ollama\nOther OpenAI-compatible server\nCustom adapter", SKY, BLUE),
        ((815, 610, 1385, 805), "EmbeddingModel", "Ollama embedding model\nHosted compatible endpoint\nCustom adapter", TEAL_BG, TEAL),
        ((1520, 610, 2090, 805), "VectorStore", "SQLite in V1\nPostgreSQL + pgvector later\nAlternative adapter", AMBER_BG, AMBER),
    ]
    for box, title, sub, fill, color in branches:
        card(draw, box, title, sub, fill, color, color)
        arrow(draw, (1100, 430), ((box[0] + box[2]) // 2, box[1] - 10), color)
    config_boxes = [
        (150, 970, 650, 1125, "base URL + model + key"),
        (850, 970, 1350, 1125, "base URL + model + key"),
        (1550, 970, 2050, 1125, "type + path or JDBC URL"),
    ]
    for x1, y1, x2, y2, text in config_boxes:
        card(draw, (x1, y1, x2, y2), text, "Configuration changes select implementations", LIGHT, LINE, NAVY)
    draw.text((90, 1210), "Embedding-model changes require re-indexing. Chat-model changes do not.", font=font(28, True), fill=AMBER)
    save_diagram(img, "02-provider-neutral-design")

    # Diagram 3: local developer experience and external requirements.
    img, draw = canvas("Local-first runtime", "Default V1 requires no Oracle infrastructure, cloud account or database server")
    card(draw, (120, 250, 720, 535), "Java application", "Spring Boot + Maven\nPDFBox + SQLite JDBC\nRuns on localhost:8080", SKY, "#98BDF8", BLUE)
    card(draw, (1480, 250, 2080, 535), "Ollama", "OpenAI-compatible HTTP\nChat model + embedding model\nRuns on localhost:11434", TEAL_BG, "#74CFC2", TEAL)
    arrow(draw, (730, 392), (1470, 392), BLUE, 10)
    centered_multiline(draw, (780, 280, 1420, 375), "HTTP requests", 27, BLUE, True)
    centered_multiline(draw, (780, 405, 1420, 510), "Chat completions\nand embeddings", 25, SLATE)
    card(draw, (120, 720, 720, 1005), "Local data", "./data/rag.db\n./data/documents/\nNo database server", AMBER_BG, "#E7B458", AMBER)
    arrow(draw, (420, 545), (420, 710), AMBER, 10)
    card(draw, (1480, 720, 2080, 1005), "Optional alternatives", "Another local model server\nHosted compatible provider\nPostgreSQL + pgvector later", LIGHT, LINE, NAVY)
    arrow(draw, (1780, 710), (1780, 545), SLATE, 8)
    draw.rounded_rectangle((760, 710, 1440, 1015), radius=28, fill=NAVY, outline=NAVY)
    centered_multiline(draw, (800, 745, 1400, 835), "What is already installed", 32, WHITE, True)
    centered_multiline(draw, (800, 840, 1400, 970), "Java 21  |  Maven 3.9.9\nOllama 0.32.14  |  Docker 29.3.0\nSQLite 3.51.0", 27, "#D7E4F2", False, 16)
    draw.rounded_rectangle((120, 1120, 2080, 1225), radius=20, fill="#FFF1F2", outline="#FDA4AF", width=3)
    centered_multiline(draw, (160, 1130, 2040, 1215), "One remaining prerequisite for real RAG: pull an embedding model. No model download is needed for fake-provider tests.", 29, "#9F1239", True)
    save_diagram(img, "03-local-first-runtime")


def inline_markup(text: str) -> str:
    safe = html.escape(text)
    safe = re.sub(r"`([^`]+)`", r'<font name="Courier">\1</font>', safe)
    safe = re.sub(r"\*\*([^*]+)\*\*", r"<b>\1</b>", safe)
    return safe


def make_styles():
    styles = getSampleStyleSheet()
    return {
        "h1": ParagraphStyle("H1", parent=styles["Heading1"], fontName="Helvetica-Bold", fontSize=20, leading=24, textColor=colors.HexColor(NAVY), spaceBefore=10, spaceAfter=10),
        "h2": ParagraphStyle("H2", parent=styles["Heading2"], fontName="Helvetica-Bold", fontSize=15, leading=18, textColor=colors.HexColor(BLUE), spaceBefore=14, spaceAfter=7, keepWithNext=True),
        "h3": ParagraphStyle("H3", parent=styles["Heading3"], fontName="Helvetica-Bold", fontSize=11.5, leading=14, textColor=colors.HexColor(TEAL), spaceBefore=9, spaceAfter=5, keepWithNext=True),
        "body": ParagraphStyle("Body", parent=styles["BodyText"], fontName="Helvetica", fontSize=9.3, leading=13.2, textColor=colors.HexColor(NAVY), spaceAfter=6),
        "bullet": ParagraphStyle("Bullet", parent=styles["BodyText"], fontName="Helvetica", fontSize=9.1, leading=12.8, leftIndent=16, firstLineIndent=-8, textColor=colors.HexColor(NAVY), spaceAfter=3),
        "small": ParagraphStyle("Small", parent=styles["BodyText"], fontName="Helvetica", fontSize=7.3, leading=9.5, textColor=colors.HexColor(NAVY)),
        "table_head": ParagraphStyle("TableHead", parent=styles["BodyText"], fontName="Helvetica-Bold", fontSize=7.2, leading=9, textColor=colors.white),
        "code": ParagraphStyle("Code", parent=styles["Code"], fontName="Courier", fontSize=7.8, leading=10, leftIndent=10, rightIndent=10, borderColor=colors.HexColor(LINE), borderWidth=0.5, borderPadding=8, backColor=colors.HexColor(LIGHT), spaceBefore=4, spaceAfter=8),
    }


def table_flowable(rows, styles):
    wrapped = []
    for r_index, row in enumerate(rows):
        style = styles["table_head"] if r_index == 0 else styles["small"]
        wrapped.append([Paragraph(inline_markup(cell.strip()), style) for cell in row])
    cols = len(rows[0])
    available = 7.33 * inch
    # Give the first columns more room for descriptive tables while preserving full width.
    if cols == 2:
        widths = [available * 0.29, available * 0.71]
    elif cols == 3:
        widths = [available * 0.25, available * 0.37, available * 0.38]
    elif cols == 4:
        widths = [available * 0.18, available * 0.30, available * 0.39, available * 0.13]
    else:
        widths = [available / cols] * cols
    table = LongTable(wrapped, colWidths=widths, repeatRows=1, hAlign="LEFT")
    table.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor(NAVY)),
        ("TEXTCOLOR", (0, 0), (-1, 0), colors.white),
        ("VALIGN", (0, 0), (-1, -1), "TOP"),
        ("GRID", (0, 0), (-1, -1), 0.45, colors.HexColor(LINE)),
        ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.HexColor(LIGHT)]),
        ("LEFTPADDING", (0, 0), (-1, -1), 5),
        ("RIGHTPADDING", (0, 0), (-1, -1), 5),
        ("TOPPADDING", (0, 0), (-1, -1), 4),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 4),
    ]))
    return table


def parse_markdown(styles):
    lines = PLAN_MD.read_text(encoding="utf-8").splitlines()
    story = []
    paragraph = []
    in_code = False
    code_lines = []

    diagram_after = {
        "## 3. How RAG works": DIAGRAM_DIR / "01-system-architecture.png",
        "## 5. Plug-and-play model design": DIAGRAM_DIR / "02-provider-neutral-design.png",
        "## 4. Models and external dependencies": DIAGRAM_DIR / "03-local-first-runtime.png",
    }

    def flush_paragraph():
        if paragraph:
            story.append(Paragraph(inline_markup(" ".join(paragraph)), styles["body"]))
            paragraph.clear()

    i = 0
    while i < len(lines):
        line = lines[i]
        if line.startswith("```"):
            flush_paragraph()
            if in_code:
                story.append(Preformatted("\n".join(code_lines), styles["code"])); code_lines.clear(); in_code = False
            else:
                in_code = True
            i += 1
            continue
        if in_code:
            code_lines.append(line)
            i += 1
            continue
        if line.startswith("|") and i + 1 < len(lines) and re.match(r"^\|[\s:|-]+\|$", lines[i + 1]):
            flush_paragraph()
            rows = []
            rows.append([c for c in line.strip().strip("|").split("|")])
            i += 2
            while i < len(lines) and lines[i].startswith("|"):
                rows.append([c for c in lines[i].strip().strip("|").split("|")])
                i += 1
            story.append(table_flowable(rows, styles))
            story.append(Spacer(1, 8))
            continue
        if line.startswith("# "):
            flush_paragraph()
            # The document title is already represented by the cover.
            i += 1
            continue
        if line.startswith("## "):
            flush_paragraph()
            story.append(Paragraph(inline_markup(line[3:]), styles["h2"]))
            if line in diagram_after:
                path = diagram_after[line]
                story.append(Image(str(path), width=7.15 * inch, height=4.225 * inch))
                story.append(Spacer(1, 8))
            i += 1
            continue
        if line.startswith("### "):
            flush_paragraph(); story.append(Paragraph(inline_markup(line[4:]), styles["h3"])); i += 1; continue
        if re.match(r"^\d+\. ", line):
            flush_paragraph(); story.append(Paragraph(inline_markup(line), styles["bullet"], bulletText=None)); i += 1; continue
        if line.startswith("- "):
            flush_paragraph(); story.append(Paragraph(inline_markup(line[2:]), styles["bullet"], bulletText="-")); i += 1; continue
        if line.strip() == "":
            flush_paragraph(); i += 1; continue
        if line.startswith("Prepared:") or line.startswith("Status:"):
            i += 1; continue
        paragraph.append(line.strip())
        i += 1
    flush_paragraph()
    return story


def page_decor(canvas_obj, doc):
    page = canvas_obj.getPageNumber()
    canvas_obj.saveState()
    if page > 1:
        canvas_obj.setStrokeColor(colors.HexColor(LINE))
        canvas_obj.line(42, LETTER[1] - 35, LETTER[0] - 42, LETTER[1] - 35)
        canvas_obj.setFont("Helvetica", 7.5)
        canvas_obj.setFillColor(colors.HexColor(SLATE))
        canvas_obj.drawString(42, LETTER[1] - 27, "MINI RAG ENGINE - PROJECT PLAN")
        canvas_obj.drawRightString(LETTER[0] - 42, 24, f"Page {page}")
    canvas_obj.restoreState()


def build_pdf():
    styles = make_styles()
    doc = SimpleDocTemplate(
        str(OUTPUT_PDF), pagesize=LETTER,
        rightMargin=42, leftMargin=42, topMargin=48, bottomMargin=38,
        title="Mini RAG Engine - Project Plan",
        author="Project planning discussion",
        subject="Local-first, provider-neutral Java RAG implementation plan",
    )
    story = [
        Spacer(1, 1.05 * inch),
        Paragraph("MINI RAG ENGINE", ParagraphStyle("CoverKicker", fontName="Helvetica-Bold", fontSize=14, leading=17, textColor=colors.HexColor(TEAL), alignment=TA_CENTER, spaceAfter=14)),
        Paragraph("Project Plan", ParagraphStyle("CoverTitle", fontName="Helvetica-Bold", fontSize=34, leading=39, textColor=colors.HexColor(NAVY), alignment=TA_CENTER, spaceAfter=18)),
        Paragraph("A local-first, provider-neutral document question-answering system built in Java without LangChain", ParagraphStyle("CoverSub", fontName="Helvetica", fontSize=15, leading=21, textColor=colors.HexColor(SLATE), alignment=TA_CENTER, leftIndent=34, rightIndent=34, spaceAfter=35)),
        Image(str(DIAGRAM_DIR / "03-local-first-runtime.png"), width=7.1 * inch, height=4.195 * inch),
        Spacer(1, 20),
        Paragraph("Planning baseline | 18 August 2026", ParagraphStyle("CoverDate", fontName="Helvetica-Bold", fontSize=10, leading=13, textColor=colors.HexColor(BLUE), alignment=TA_CENTER)),
        PageBreak(),
        Paragraph("Executive decision", styles["h1"]),
        Paragraph("Proceed with Java 21, Spring Boot, Maven, PDFBox, embedded SQLite, exact cosine search in Java, and independently configurable OpenAI-compatible chat and embedding clients. Use Ollama locally by default and deterministic fake providers for automated tests. No Oracle infrastructure or cloud account is required.", styles["body"]),
        Paragraph("Readiness", styles["h2"]),
        table_flowable([
            ["Ready now", "Remaining before real end-to-end RAG"],
            ["Java 21, Maven 3.9.9, running Ollama 0.32.14, Docker, SQLite", "Pull an embedding model such as embeddinggemma; select a chat model; allow Maven to obtain dependencies on the first build"],
        ], styles),
        Spacer(1, 10),
        Paragraph("Document map", styles["h2"]),
        Paragraph("Sections 1-3 define the product and RAG flow. Sections 4-6 define external models, provider portability, and storage. Sections 7-9 specify architecture, APIs, and implementation phases. Sections 10-14 cover testing, acceptance, risks, learning outcomes, and the recommended decisions.", styles["body"]),
        PageBreak(),
    ]
    story.extend(parse_markdown(styles))
    doc.build(story, onFirstPage=page_decor, onLaterPages=page_decor)


if __name__ == "__main__":
    build_diagrams()
    build_pdf()
    print(OUTPUT_PDF)
