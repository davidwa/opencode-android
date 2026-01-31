#!/data/data/com.termux/files/usr/bin/bash
# Setup OpenCode Server di Termux

echo "=========================================="
echo "OpenCode Server Setup for Termux"
echo "=========================================="
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

cd $HOME

# Check if node is installed
if ! command -v node &> /dev/null; then
echo -e "${YELLOW}Installing Node.js...${NC}"
    pkg update -y
    pkg install nodejs-lts -y
fi

echo -e "${GREEN}✓ Node.js installed: $(node --version)${NC}"

# Install opencode
echo ""
echo -e "${YELLOW}Installing OpenCode...${NC}"
npm i -g opencode-ai

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ OpenCode installed successfully${NC}"
else
    echo -e "${RED}✗ Failed to install OpenCode${NC}"
    exit 1
fi

# Get IP address
IP_ADDRESS=$(ifconfig 2>/dev/null | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1' | head -n 1)

if [ -z "$IP_ADDRESS" ]; then
    IP_ADDRESS=$(ip addr show 2>/dev/null | grep -Eo 'inet ([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1' | head -n 1)
fi

echo ""
echo "=========================================="
echo -e "${GREEN}Setup Complete!${NC}"
echo "=========================================="
echo ""
echo "Your IP Address: $IP_ADDRESS"
echo ""
echo "To start OpenCode server:"
echo "  opencode serve --port 4096 --hostname 0.0.0.0"
echo ""
echo "Or with password:"
echo "  OPENCODE_SERVER_PASSWORD=yourpass opencode serve --port 4096 --hostname 0.0.0.0"
echo ""
echo "Connect from Android app:"
echo "  http://$IP_ADDRESS:4096"
echo ""
echo "=========================================="

# Create start script
cat > $HOME/start-opencode.sh << 'EOF'
#!/data/data/com.termux/files/usr/bin/bash
cd $HOME
IP=$(ifconfig 2>/dev/null | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1' | head -n 1)
echo "Starting OpenCode server on http://$IP:4096"
echo "Press Ctrl+C to stop"
echo ""
opencode serve --port 4096 --hostname 0.0.0.0
EOF

chmod +x $HOME/start-opencode.sh

echo "Start script created: ~/start-opencode.sh"
echo ""
