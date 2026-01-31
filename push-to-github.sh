#!/bin/bash
# GitHub Push Script for OpenCode Android
# Run this script to push to GitHub

echo "=========================================="
echo "GitHub Push Setup"
echo "=========================================="
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Configuration
GITHUB_USERNAME="mulkymalikuldhrs"
REPO_NAME="opencode-android"
REPO_URL="git@github.com:$GITHUB_USERNAME/$REPO_NAME.git"
PUBLIC_REPO_URL="https://github.com/$GITHUB_USERNAME/$REPO_NAME.git"

echo -e "${YELLOW}Repository Configuration:${NC}"
echo "Username: $GITHUB_USERNAME"
echo "Repository: $REPO_NAME"
echo ""
echo "=========================================="
echo ""

# Check if git is initialized
if [ ! -d ".git" ]; then
    echo -e "${YELLOW}Initializing git repository...${NC}"
    git init
    
    echo -e "${YELLOW}Adding all files...${NC}"
    git add .
    
    echo -e "${YELLOW}Creating initial commit...${NC}"
    git commit -m "Initial commit: OpenCode Android v1.0.0

feat: Full OpenCode API integration

- Complete API client with 50+ endpoints
- SSE event streaming for real-time updates
- Session management system
- Chat, Terminal, Files, Editor fragments
- Material Design 3 dark theme
- Termux setup scripts included

- OpenCode Android: The first full-featured OpenCode client for Android
- Built with ❤️ by Mulky Malikul Dhaher"
"

    echo -e "${GREEN}✓ Git initialized${NC}"
else
    echo -e "${GREEN}✓ Git already initialized${NC}"
fi

echo ""
echo "=========================================="
echo ""

# Add remote if not exists
if ! git remote get-url origin > /dev/null 2>&1; then
    echo -e "${YELLOW}Adding remote origin...${NC}"
    git remote add origin $REPO_URL
    echo -e "${GREEN}✓ Remote origin added${NC}"
fi

echo ""
echo "=========================================="
echo ""

# Get current branch
BRANCH=$(git rev-parse --abbrev-ref HEAD)
echo -e "${YELLOW}Current branch: $BRANCH${NC}"
echo ""

# Check if we need to create the repository on GitHub first
echo -e "${YELLOW}Before pushing, make sure to:${NC}"
echo "1. Create empty repository on GitHub: $PUBLIC_REPO_URL"
echo "2. Or use GitHub CLI: gh repo create $REPO_NAME --public"
echo ""

# Ask user if they want to proceed
read -p "Do you want to push to GitHub now? (y/n): " PROCEED

if [ "$PROCEED" != "y" ] && [ "$PROCEED" != "Y" ]; then
    echo "Cancelled."
    exit 0
fi

echo ""
echo -e "${YELLOW}Pushing to GitHub...${NC}"
echo ""

# Push to GitHub
git push -u origin $BRANCH

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo -e "${GREEN}✓ Successfully pushed to GitHub!${NC}"
    echo "=========================================="
    echo ""
    echo "Repository: $PUBLIC_REPO_URL"
    echo ""
    echo "Next steps:"
    echo "1. Visit your repository: $PUBLIC_REPO_URL"
    echo "2. Add a README if needed (already included)"
    echo "3. Add topics/tags for discoverability"
    echo "4. Share with others!"
    echo ""
else
    echo ""
    echo "=========================================="
    echo -e "${RED}✗ Failed to push to GitHub${NC}"
    echo "=========================================="
    echo ""
    echo "Troubleshooting:"
    echo "1. Make sure you're logged in: git config --global user.name"
    echo "2. Make sure your SSH key is added to GitHub"
    echo "3. Or use personal access token for HTTPS"
    echo ""
fi
