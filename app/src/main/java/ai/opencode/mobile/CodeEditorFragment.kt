package ai.opencode.mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class CodeEditorFragment : Fragment() {
    
    private lateinit var webView: WebView
    private lateinit var filenameInput: EditText
    private lateinit var saveButton: Button
    private lateinit var loadButton: Button
    
    private var currentFile: String = "untitled.txt"
    private var currentCode: String = ""
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_code_editor, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        webView = view.findViewById(R.id.webView)
        filenameInput = view.findViewById(R.id.filenameInput)
        saveButton = view.findViewById(R.id.saveButton)
        loadButton = view.findViewById(R.id.loadButton)
        
        setupWebView()
        
        saveButton.setOnClickListener { saveFile() }
        loadButton.setOnClickListener { loadFile() }
        
        // Load default content
        loadCode("""
            // Welcome to OpenCode Editor
            // Write your code here
            
            function hello() {
                console.log("Hello from OpenCode!");
                return "OpenCode is awesome! 🚀";
            }
            
            hello();
        """.trimIndent())
    }
    
    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
        
        // Load Monaco Editor or CodeMirror via CDN
        val html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { margin: 0; padding: 0; background: #1e1e1e; }
                    #editor { 
                        width: 100%; 
                        height: 100vh; 
                        background: #1e1e1e;
                        color: #d4d4d4;
                        font-family: 'Consolas', 'Monaco', monospace;
                        font-size: 14px;
                        padding: 16px;
                        border: none;
                        outline: none;
                        resize: none;
                        line-height: 1.5;
                    }
                </style>
            </head>
            <body>
                <textarea id="editor" spellcheck="false"></textarea>
                <script>
                    const editor = document.getElementById('editor');
                    
                    window.setCode = function(code) {
                        editor.value = code;
                    };
                    
                    window.getCode = function() {
                        return editor.value;
                    };
                    
                    // Simple syntax highlighting simulation
                    editor.addEventListener('input', function() {
                        // In real implementation, would use Monaco/CodeMirror
                    });
                    
                    // Tab key support
                    editor.addEventListener('keydown', function(e) {
                        if (e.key === 'Tab') {
                            e.preventDefault();
                            const start = this.selectionStart;
                            const end = this.selectionEnd;
                            this.value = this.value.substring(0, start) + '    ' + this.value.substring(end);
                            this.selectionStart = this.selectionEnd = start + 4;
                        }
                    });
                </script>
            </body>
            </html>
        """.trimIndent()
        
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    }
    
    private fun loadCode(code: String) {
        currentCode = code
        webView.evaluateJavascript("window.setCode(`${escapeJs(code)}`);", null)
    }
    
    private fun saveFile() {
        webView.evaluateJavascript("window.getCode();") { value ->
            currentCode = value?.removeSurrounding("\"") ?: ""
            val filename = filenameInput.text.toString().ifEmpty { currentFile }
            // Save to file system
            // Implementation would write to app's private directory
        }
    }
    
    private fun loadFile() {
        val filename = filenameInput.text.toString()
        if (filename.isNotEmpty()) {
            currentFile = filename
            // Load from file system
        }
    }
    
    private fun escapeJs(code: String): String {
        return code.replace("\\", "\\\\")
            .replace("`", "\\`")
            .replace("$", "\\$")
    }
}
