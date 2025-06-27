#!/bin/bash

set -e

##################################################
# Configure Git
##################################################
git config --global user.name "github-actions[bot]"
git config --global user.email "41898282+github-actions[bot]@users.noreply.github.com"

##################################################
# Set remote to use GitHub token for authentication
##################################################
git remote set-url origin https://x-access-token:${GITHUB_TOKEN}@github.com/${GITHUB_REPOSITORY}.git

##################################################
# Ensure clean pull before making changes
##################################################
git fetch origin "$GITHUB_REF_NAME"
git checkout "$GITHUB_REF_NAME"
git pull --rebase origin "$GITHUB_REF_NAME"

##################################################
# Stage, commit, and push formatting changes
##################################################
if ! git diff --quiet; then
  git add -A
  git commit -m "chore: auto-format frontend via Prettier"
  git push origin HEAD:"$GITHUB_REF_NAME"
fi

