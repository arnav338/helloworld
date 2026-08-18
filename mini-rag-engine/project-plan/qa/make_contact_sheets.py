#!/usr/bin/env python3
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parent
pages = sorted(ROOT.glob("page-*.png"))
if not pages:
    raise SystemExit("No rendered pages found")

font = ImageFont.truetype("/System/Library/Fonts/Supplemental/Arial Bold.ttf", 26)
for sheet_index in range(0, len(pages), 4):
    selected = pages[sheet_index:sheet_index + 4]
    sheet = Image.new("RGB", (1500, 1020), "#DCE3EC")
    draw = ImageDraw.Draw(sheet)
    for slot, path in enumerate(selected):
        page = Image.open(path).convert("RGB")
        # Two rows must fit fully within the 1020px contact sheet.
        page.thumbnail((700, 420))
        x = 35 + (slot % 2) * 740
        y = 70 + (slot // 2) * 490
        sheet.paste(page, (x, y))
        draw.text((x, 24 + (slot // 2) * 490), path.stem, fill="#12263A", font=font)
    sheet.save(ROOT / f"contact-{sheet_index // 4 + 1}.png", dpi=(120, 120))
