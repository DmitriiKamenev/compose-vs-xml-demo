#!/usr/bin/env python3
"""Подсчет логических строк кода (без пустых строк, комментариев, package/import и <?xml ...?>).
Запуск из корня репозитория: python3 tools/count_loc.py"""
import re
from pathlib import Path

SRC = Path("app/src/main")
K = SRC / "java/com/example/composevsxml"
R = SRC / "res"

COMPOSE = [K / "compose/DemoScreen.kt", K / "compose/ComposeDemoActivity.kt"]
XML = [R / "layout/activity_xml_demo.xml", R / "layout/item_setting_row.xml",
       R / "layout/item_switch_row.xml", R / "menu/bottom_nav.xml", K / "views/XmlDemoActivity.kt"]

def count_kt(p):
    n, in_block = 0, False
    for line in p.read_text(encoding="utf-8").splitlines():
        s = line.strip()
        if in_block:
            in_block = "*/" not in s
            continue
        if s.startswith("/*"):
            in_block = "*/" not in s
            continue
        if not s or s.startswith(("//", "package ", "import ")):
            continue
        n += 1
    return n

def count_xml(p):
    text = re.sub(r"<!--.*?-->", "", p.read_text(encoding="utf-8"), flags=re.S)
    return sum(1 for l in text.splitlines() if l.strip() and not l.strip().startswith("<?xml"))

def count(p):
    return count_kt(p) if p.suffix == ".kt" else count_xml(p)

def report(title, files):
    total = 0
    print(title)
    for f in files:
        c = count(f)
        total += c
        print(f"  {c:4d}  {f.relative_to(SRC)}")
    print(f"  {total:4d}  ИТОГО, файлов: {len(files)}\n")
    return total

c = report("Jetpack Compose", COMPOSE)
x = report("XML + View System", XML)
print(f"Compose короче на {100 * (1 - c / x):.0f} % ({c} против {x} строк)")
