#!/bin/bash

set -e

cd backend

##################################################
#
# Configure Git user
#
##################################################
git config --global user.name "github-actions[bot]"
git config --global user.email "41898282+github-actions[bot]@users.noreply.github.com"

##################################################
#
# Set the remote to use GitHub token for authentication
#
##################################################
git remote set-url origin https://x-access-token:${GITHUB_TOKEN}@github.com/${GITHUB_REPOSITORY}.git

##################################################
#
# Stage any formatting changes
#
##################################################
git add .
if ! git diff --cached --quiet; then
  git commit -m "chore: auto-format backend via Spotless"
fi

##################################################
#
# Pull with rebase to prevent push conflicts
#
##################################################
git pull --rebase origin "$GITHUB_REF_NAME"

##################################################
#
# Push only if there were actual changes
#
##################################################
if ! git diff --cached --quiet; then
  git push origin HEAD:"$GITHUB_REF_NAME"
fi

