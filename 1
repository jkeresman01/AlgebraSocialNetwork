#!/bin/bash

set -e

cd frontend

##################################################
#
# Configure Git
#
##################################################
git config --global user.name "github-actions[bot]"
git config --global user.email "41898282+github-actions[bot]@users.noreply.github.com"

##################################################
#
# Set the remote URL to use the GitHub token for authentication
#
##################################################
git remote set-url origin https://x-access-token:${GITHUB_TOKEN}@github.com/${GITHUB_REPOSITORY}.git

##################################################
#
# Stage and commit formatting changes if any
#
##################################################
git add .
if ! git diff --cached --quiet; then
  git commit -m "chore: auto-format frontend via Prettier"
fi

##################################################
#
# Pull with rebase to avoid conflicts
#
##################################################
git pull --rebase origin "$GITHUB_REF_NAME"

##################################################
#
# Push if commit exists
#
##################################################
if ! git diff --cached --quiet; then
  git push origin HEAD:"$GITHUB_REF_NAME"
fi

