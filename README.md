# 🤖 Opencode Android Client

<div align="center">

[![Opencode Android Logo](https://img.shields.io/badge/Opencode-Android-v1.0.0-purple)](https://github.com/mulkymalikuldhrs/opencode-android/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Platform: Android](https://img.shields.io/badge/Platform-Android-blue.svg)]()
[![GitHub Stars](https://img.shields.io/github/stars/mulkymalikuldhrs/opencode-android?style=social)](https://github.com/mulkymalikuldhrs/opencode-android/stargazers)
[![GitHub Issues](https://img.shields.io/github/issues/mulkymalikuldhrs/opencode-android?style=social)](https://github.com/mulkymalikuldhrs/opencode-android/issues)
[![GitHub Issues](https://img.shields.io/github/issues-closed/mulkymalikuldhrs/opencode-android?style=social)](https://github.com/mulkymalikuldhrs/opencode-android/issues?q=is%3Aopen+is%3Atrue)

**⚠️ Independent OpenCode Android Client - Community Build**

</div>

---

## ✨ Features

### 🚀 Core Capabilities
- **Full OpenCode API Integration** - Connect to OpenCode server (50+ endpoints)
- **Real-Time Chat** - SSE (Server-Sent Events) streaming for instant AI responses
- **Session Management** - Create, fork, switch between sessions
- **Terminal Access** - Execute shell commands on server
- **File Browser** - Read, search, and manage project files
- **Code Editor** - Full-featured code editing with syntax highlighting
- **Multi-Provider Support** - OpenAI, Anthropic Claude, 75+ LLM providers

### 📱 Android Native
- **Material Design 3** - Beautiful dark theme with gradient accents
- **Bottom Navigation** - Intuitive 4-tab layout
- **Coroutines** - Asynchronous, non-blocking operations
- **Auto-Reconnection** - Maintains connection stability
- **Background Service** - Keeps sessions alive

### 🎯 Developer Tools
- **Real AI Responses** - Powered by OpenCode server
- **File Diff Tracking** - See code changes across sessions
- **Command Execution** - Execute commands via OpenCode shell API
- **Git Integration** - VCS operations through OpenCode
- **LSP Support** - Language Server Protocol for code intelligence
- **MCP Protocol** - Model Context Protocol for advanced integrations

---

## 📸 Screenshots

| Chat Interface | Terminal | File Manager | Code Editor |
|:-----------:|:----------:|:------------:|:------------:|
| ![Chat](docs/screenshots/chat.png) | ![Terminal](docs/screenshots/terminal.png) | ![Files](docs/screenshots/files.png) | ![Editor](docs/screenshots/editor.png) |

*Full UI implementation with real OpenCode backend connectivity*

---

## 🚀 Getting Started

### Prerequisites

**Android Device:**
- Android 7.0 (API 24) or higher
- 2GB+ RAM recommended
- 500MB+ storage space

**Backend (Required):**
- **Option 1: Termux** (Recommended for Android)
  ```bash
  pkg update -y
  pkg install nodejs-lts -y
  npm i -g opencode-ai
  opencode serve --port 4096
  ```

- **Option 2: PC/Mac/Linux**
  ```bash
  npm install -g opencode-ai
  opencode serve --port 4096
  ```

### Installation

#### Method 1: APK Install
```bash
# Build APK
./build.sh

# Install via ADB
adb install app/build/outputs/apk/release/app-release-unsigned.apk

# Or sideload APK directly
```

#### Method 2: Android Studio
```bash
# Clone repository
git clone https://github.com/mulkymalikuldhrs/opencode-android.git
cd opencode-android

# Open in Android Studio
# Sync Gradle
# Build & Run
```

### First Run

1. **Launch App**
2. **Connect to OpenCode Server**
   - Enter server URL: `http://<your-ip>:4096`
   - Enter password (if configured)
3. **Start Coding!**
   - Create a session
   - Chat with AI
   - Execute commands
   - Browse files
   - Edit code

---

## 📚 API Documentation

### Core Endpoints

#### Global
```
GET  /global/health
Response: { healthy: true, version: "1.0.193" }

GET  /global/event
Returns: SSE event stream (real-time updates)
```

#### Sessions
```
GET  /session
List all sessions

POST  /session
Body: { title?: string, parentID?: string }
Response: { id, title, createdAt, updatedAt, status, ... }

GET  /session/:id
Get session details

POST  /session/:id/message
Body: { messageID?, model?, agent?, system?, parts: [...] }
Response: { info: Message, parts: [...] }

POST  /session/:id/shell
Body: { agent?, model?, command: string }
Response: { info: Message, parts: [...] }

POST  /session/:id/command
Body: { command, arguments: string[] }
Response: { info: Message, parts: [...] }

DELETE  /session/:id
Delete session and all data

POST  /session/:id/fork
Body: { messageID? }
Response: New session

POST  /session/:id/abort
Abort running session

GET  /session/:id/diff
Get file diffs for session
```

#### Files
```
GET  /file?path=<path>
List files and directories
Response: FileNode[]

GET  /file/content?path=<path>
Read file contents
Response: { path, content, encoding }

GET  /find?pattern=<pattern>
Search text in files
Response: { path, lines, lineNumber, absoluteOffset, submatches }[]

GET  /find/file?query=<q>
Find files by name
Response: string[] (paths)
```

#### Providers
```
GET  /provider
List all providers
Response: { all: Provider[], default: { key: string }, connected: string[] }

GET  /provider/auth
Get provider authentication methods
Response: { [providerID: string]: ProviderAuthMethod[] }

POST  /provider/:id/oauth/authorize
OAuth authorization flow
```

#### Commands
```
GET  /command
List all available commands
Response: Command[]
```

### Authentication

OpenCode uses HTTP Basic Authentication:

```kotlin
// Client-side
val client = OpenCodeClient(
    serverUrl = "http://192.168.1.100:4096",
    username = "opencode",
    password = "your-password"
)
```

### Event Types (SSE)

```typescript
type ServerEvent = 
  | { type: "server.connected", data: {} }
  | { type: "session.status", data: { sessionID: string, status: string } }
  | { type: "message", data: { sessionID: string, message: Message } }
  | { type: "command.output", data: { sessionID: string, output: string } }
  | { type: "error", data: { error: string } }
```

---

## 🔄 Changelog

### Version 1.0.0 (2026-01-31)

#### 🎉 Initial Release

**Added:**
- ✅ Full OpenCode API client (50+ endpoints)
- ✅ SSE event streaming for real-time updates
- ✅ Session management (create, delete, fork, abort)
- ✅ Chat interface with message history
- ✅ Terminal emulator with command execution
- ✅ File manager with browsing and search
- ✅ Code editor with syntax highlighting
- ✅ Multi-provider support (OpenAI, Claude, 75+ LLMs)
- ✅ Connection wizard with health check
- ✅ Persistent connection settings
- ✅ Background service for session keep-alive
- ✅ Material Design 3 dark theme
- ✅ Bottom navigation with 4 tabs
- ✅ Type-safe data models
- ✅ Coroutines for async operations

**Tech Stack:**
- Kotlin (100%)
- OkHttp for HTTP/SSE
- AndroidX libraries
- Material Design Components
- Coroutines + Flow

---

## 🔧 Configuration

### Environment Variables
```bash
# Server URL
OPENCODE_SERVER_URL=http://192.168.1.100:4096

# Server Password (optional)
OPENCODE_SERVER_PASSWORD=your-password
```

### Client Configuration
```kotlin
// Stored in SharedPreferences
class Config {
    var serverUrl: String = "http://localhost:4096"
    var username: String = "opencode"
    var password: String = ""
    var autoConnect: Boolean = false
}
```

---

## 🤝 Credits

### 👤 Author
**Mulky Malikul Dhaher**

### 📧 Contact
- **Email:** mulkymalikuldhrs@email.com
- **GitHub:** [@mulkymalikuldhrs](https://github.com/mulkymalikuldhrs)
- **Social:** [@mulkymalikuldhr](https://instagram.com/mulkymalikuldhr) (FB/IG)

### 🙏 Acknowledgments

This project is based on and inspired by:
- [OpenCode](https://opencode.ai) - The open source AI coding agent
- [Anomaly](https://anomaly.co) - OpenCode creators
- Material Design - Google's design system
- OkHttp - Square's HTTP client
- Kotlin Programming Language - JetBrains

---

## 🌐 Links

- **GitHub Repository:** https://github.com/mulkymalikuldhrs/opencode-android
- **OpenCode Docs:** https://opencode.ai/docs
- **OpenCode GitHub:** https://github.com/anomalyco/opencode
- **Bug Tracker:** https://github.com/mulkymalikuldhrs/opencode-android/issues
- **Feature Requests:** https://github.com/mulkymalikuldhrs/opencode-android/discussions
- **Releases:** https://github.com/mulkymalikuldhrs/opencode-android/releases

---

## 📄 License

```
MIT License

Copyright (c) 2026 Mulky Malikul Dhaher

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 📱 Source Code
```bash
git clone https://github.com/mulkymalikuldhrs/opencode-android.git
```

---

## 🌟 Star History

[![Star History Chart](https://api.star-history.com/svg?repos=mulkymalikuldhrs/opencode-android&type=Date)](https://star-history.com/#mulkymalikuldhrs/opencode-android&Date)

---

## 📊 Project Statistics

- **Total Files:** 50+
- **Kotlin Files:** 12
- **XML Layouts:** 15
- **API Endpoints:** 50+
- **Dependencies:** 8
- **Lines of Code:** 8,000+
- **Contributors:** 1
- **Forks:** 0
- **Stars:** [![GitHub Stars](https://img.shields.io/github/stars/mulkymalikuldhrs/opencode-android?style=social)](https://github.com/mulkymalikuldhrs/opencode-android/stargazers)

---

<div align="center">

## ⭐ Star This Project!

If you find OpenCode Android useful, please give it a star on GitHub!

[![Stargazers repo mulkymalikuldhrs/opencode-android](https://reporoster.star-history.com/star/mulkymalikuldhrs/opencode-android)](https://github.com/mulkymalikuldhrs/opencode-android/stargazers)

Made with ❤️ by [Mulky Malikul Dhaher](https://github.com/mulkymalikuldhrs) <!-- [Portfolio](https://portomulky.vercel.app) -->

---

[![Built with Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple)](https://kotlinlang.org/)
[![Android API](https://img.shields.io/badge/API-24%2B34-blue)](https://developer.android.com/)
[![Target SDK](https://img.shields.io/badge/Target-34%2B34-green)](https://developer.android.com/)

</div>
