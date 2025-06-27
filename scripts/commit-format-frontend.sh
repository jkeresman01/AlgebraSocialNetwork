#!/bin/bash
set -e

##################################################
# Configure Git
##################################################
git config --global user.name "github-actions[bot]"
git config --global user.email "41898282+github-actions[bot]@users.noreply.github.com"

##################################################
# Set remote URL to use GitHub token for authentication
##################################################
git remote set-url origin https://x-access-token:${GITHUB_TOKEN}@github.com/${GITHUB_REPOSITORY}.git

##################################################
# Stage and commit formatting changes if any
##################################################
git add -A
if ! git diff --cached --quiet; then
  git commit -m "Fix some formatting nonsense"
fi

git fetch origin "$GITHUB_REF_NAME"
git merge --strategy-option=theirs origin/"$GITHUB_REF_NAME" || true

##################################################
# Push if there are changes
##################################################
git push origin HEAD:"$GITHUB_REF_NAME"

