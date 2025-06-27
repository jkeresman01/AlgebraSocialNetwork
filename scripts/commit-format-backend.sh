#!/bin/bash
set -e

git config --global user.name "github-actions[bot]"
git config --global user.email "41898282+github-actions[bot]@users.noreply.github.com"

git add .
if ! git diff --cached --quiet; then
  git commit -m "chore: auto-format backend via Spotless"
  git push
fi

