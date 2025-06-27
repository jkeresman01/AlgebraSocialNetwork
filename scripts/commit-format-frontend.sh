#!/bin/bash
set -e

git config --global user.name "github-actions[bot]"
git config --global user.email "41898282+github-actions[bot]@users.noreply.github.com"
git remote set-url origin https://x-access-token:${GITHUB_TOKEN}@github.com/${GITHUB_REPOSITORY}.git

git add .
git diff --cached --quiet || git commit -m "Fix formatting nonsense"
git pull --rebase origin "$GITHUB_REF_NAME"
git push origin HEAD:refs/heads/${GITHUB_REF_NAME}

