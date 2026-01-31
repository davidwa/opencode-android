#!/data/data/com.termux/files/usr/bin/bash
# Quick Start OpenCode Server in Termux
# Copy this script to Termux and run: bash start-opencode-termux.sh

echo "=========================================="
echo "🚀 OpenCode Server - Quick Start"
echo "=========================================="

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

# Check if opencode is installed
if ! command -v opencode &> /dev/null; then
    echo "OpenCode not found. Installing..."
    
    # Update packages
    pkg update -y
    
    # Install Node.js
    pkg install nodejs-lts -y
    
    # Install OpenCode
    npm i -g opencode-ai
    
    echo -e "${GREEN}✓ OpenCode installed${NC}"
fi

# Get IP address
IP_ADDRESS=$(ifconfig 2>/dev/null | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1' | head -n 1)

if [ -z "$IP_ADDRESS" ]; then
    IP_ADDRESS=$(ip addr show 2>/dev/null | grep -Eo 'inet ([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1' | head -n 1)
fi

echo ""
echo -e "${BLUE}📱 Your Device IP: $IP_ADDRESS${NC}"
echo ""
echo "Connect from OpenCode Android app:"
echo "  URL: http://$IP_ADDRESS:4096"
echo ""
echo "Starting server..."
echo "Press Ctrl+C to stop"
echo "=========================================="
echo ""

# Keep screen on
termux-wake-lock

# Start OpenCode server
opencode serve --port 4096 --hostname 0.0.0.0

# Release wake lock when done
termux-wake-unlock
