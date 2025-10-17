# HeartsV

[![Coverage Status](https://coveralls.io/repos/github/HeartsV/Hearts/badge.svg)](https://coveralls.io/github/HeartsV/Hearts)
[![Coverage Status](https://coveralls.io/repos/github/HeartsV/Hearts/badge.svg?branch=main)](https://coveralls.io/github/HeartsV/Hearts?branch=main)

Ein Scala-Projekt zur Umsetzung des Kartenspiels Hearts im Rahmen einer Vorlesung.

## Projektstruktur

- `src/main/scala/de/htwg/se/Hearts` – Hauptcode
- `src/test//scala/de/htwg/se/Hearts` – Tests
- `build.sbt` – Build-Konfiguration
- `.github/workflows/ci.yml` – GitHub Actions Workflow

## Continuous Integration

Dieses Projekt verwendet GitHub Actions zur automatischen Ausführung von Tests bei jedem Commit. Die Testabdeckung wird mit scoverage ermittelt und über Coveralls veröffentlicht.

## Tests ausführen

Lokal kannst du die Tests und die Coverage-Berichte mit folgendem Befehl ausführen:

```bash
sbt clean coverage test coverageReport
```
### Mit Coverage Report on Coveralls

```bash
sbt clean coverage test
```

Credits:
Card backside:
https://commons.wikimedia.org/wiki/File:Reverso_baraja_española_rojo.svg

card-deck:
https://opengameart.org/content/playing-cards-vector-png
We editet the Aces a little bit with Inkscape. Just reduced the size of the symboles in the center of the cards.
Converted the cards zu pngs by our self with a small python script: convert_svg_to_png.py

## TUI symbols
If the consol shows strange/unexpected texts you can tipe in chcp65001

