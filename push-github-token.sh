#!/bin/bash
# GitHub Push Script with Personal Access Token
# Author: Mulky Malikul Dhaher
# Token: Fine-grained classic PAT (30 days)

echo "=========================================="
echo "GitHub Push with Personal Access Token"
echo "=========================================="
echo ""

# Configuration
GITHUB_USERNAME="mulkymalikuldhrs"
REPO_NAME="opencode-android"
REPO_URL="https://github.com/$GITHUB_USERNAME/$REPO_NAME.git"

# Your Fine-Grained Classic PAT (30 days)
# IMPORTANT: Set this as environment variable or pass as argument
# DO NOT hardcode tokens in scripts for security
GITHUB_TOKEN="${GITHUB_TOKEN:-}"

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${YELLOW}Repository Configuration:${NC}"
echo "Username: $GITHUB_USERNAME"
echo "Repository: $REPO_NAME"
echo "URL: $REPO_URL"
echo "Token: ${GITHUB_TOKEN:0:8}...masked (fine-grained classic, 30 days)"
echo ""
echo "=========================================="
echo ""

# Check if git is initialized
if [ ! -d ".git" ]; then
    echo -e "${YELLOW}Initializing git repository...${NC}"
    git init
    echo -e "${GREEN}✓ Git initialized${NC}"
else
    echo -e "${GREEN}✓ Git already initialized${NC}"
fi

# Check if remote exists
if ! git remote get-url origin > /dev/null 2>&1; then
    echo -e "${YELLOW}Adding remote origin...${NC}"
    # Create authenticated URL with token
    AUTH_URL="https://${GITHUB_TOKEN}@github.com/$GITHUB_USERNAME/$REPO_NAME.git"
    git remote add origin $AUTH_URL
    echo -e "${GREEN}✓ Remote origin added (authenticated with token)${NC}"
else
    # Update remote to use token
    echo -e "${YELLOW}Updating remote origin with token...${NC}"
    AUTH_URL="https://${GITHUB_TOKEN}@github.com/$GITHUB_USERNAME/$REPO_NAME.git"
    git remote set-url origin $AUTH_URL
    echo -e "${GREEN}✓ Remote origin updated${NC}"
fi

# Add all files
echo -e "${YELLOW}Adding all files...${NC}"
git add .

# Check status
STATUS=$(git status --porcelain)
if [ -z "$STATUS" ]; then
    echo -e "${YELLOW}No changes to commit${NC}"
    echo "Pushing existing commits..."
else
    echo -e "${YELLOW}Files to commit:$(git status --short | wc -l)${NC}"
    
    # Create commit
    echo -e "${YELLOW}Creating commit...${NC}"
    git commit -m "feat: OpenCode Android v1.0.0 - Full OpenCode Client for Android

Features:
- Full OpenCode API integration (50+ endpoints)
- SSE event streaming for real-time updates
- Session management (create, delete, fork, abort)
- Chat interface with message history
- Terminal emulator with command execution
- File manager with browsing and search
- Code editor with syntax highlighting
- Multi-provider support (OpenAI, Claude, Anthropic, 75+ LLMs)
- Material Design 3 dark theme
- Bottom navigation (Chat, Terminal, Files, Editor)
- Connection wizard with health check
- Background service for session keep-alive
- Persistent connection settings
- Type-safe data models with Coroutines

Tech Stack:
- Kotlin (100%)
- OkHttp for HTTP/SSE client
- AndroidX libraries
- Material Design Components
- Coroutines + Flow for reactive programming

Built by Mulky Malikul Dhaher
Email: mulkymalikuldhrs@email.com
GitHub: https://github.com/mulkymalikuldhrs/opencode-android

Keywords: AI, OpenCode, Android, Coding, Terminal, IDE, LLM, GPT, Claude, OpenAI, Anthropic, Developer Tools, Mobile Development"

This is an independent Android client application for OpenCode AI coding agent.
Based on OpenCode by Anomaly and licensed under MIT."
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Commit created${NC}"
    else
        echo -e "${RED}✗ Commit failed${NC}"
        exit 1
    fi
fi

echo ""
echo "=========================================="
echo ""

# Determine branch
BRANCH=$(git rev-parse --abbrev-ref HEAD)
echo -e "${YELLOW}Pushing branch: $BRANCH${NC}"
echo ""

# Push to GitHub
echo -e "${YELLOW}Pushing to GitHub...${NC}"
git push -u origin $BRANCH

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo -e "${GREEN}✓ Successfully pushed to GitHub!${NC}"
    echo "=========================================="
    echo ""
    echo "Repository: $REPO_URL"
    echo "Branch: $BRANCH"
    echo "Files pushed: $(git rev-list --count)"
    echo ""
    echo "Next steps:"
    echo "1. Visit your repository: $REPO_URL"
    echo "2. Enable GitHub Pages (optional) for website"
    echo "3. Add topics/tags for discoverability"
    echo "4. Share with others!"
    echo ""
    echo "Token info:"
    echo "  Type: Fine-grained Classic PAT"
    echo "  Expiration: 30 days"
    echo "  Scope: repo (full control)"
    echo ""
else
    echo ""
    echo "=========================================="
    echo -e "${RED}✗ Failed to push to GitHub${NC}"
    echo "=========================================="
    echo ""
    echo "Troubleshooting:"
    echo "1. Check your token: https://github.com/settings/tokens"
    echo "2. Verify token has 'repo' scope"
    echo "3. Make sure token hasn't expired"
    echo "4. Check your internet connection"
    echo "5. Verify repository URL: $REPO_URL"
    echo ""
    exit 1
fi
