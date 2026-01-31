package ai.opencode.mobile

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.io.File

class FileManagerFragment : Fragment() {
    
    private lateinit var currentPathText: TextView
    private lateinit var fileListView: ListView
    private lateinit var upButton: Button
    
    private var currentDir: File = Environment.getExternalStorageDirectory()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_file_manager, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        currentPathText = view.findViewById(R.id.currentPathText)
        fileListView = view.findViewById(R.id.fileListView)
        upButton = view.findViewById(R.id.upButton)
        
        upButton.setOnClickListener { navigateUp() }
        
        refreshFileList()
    }
    
    private fun refreshFileList() {
        currentPathText.text = currentDir.absolutePath
        
        val files = currentDir.listFiles()?.toList() ?: emptyList()
        val fileNames = files.map { 
            if (it.isDirectory) "📁 ${it.name}/" else "📄 ${it.name}"
        }.toTypedArray()
        
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, fileNames)
        fileListView.adapter = adapter
        
        fileListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedFile = files[position]
            if (selectedFile.isDirectory) {
                currentDir = selectedFile
                refreshFileList()
            } else {
                openFile(selectedFile)
            }
        }
    }
    
    private fun navigateUp() {
        val parent = currentDir.parentFile
        if (parent != null && parent.canRead()) {
            currentDir = parent
            refreshFileList()
        }
    }
    
    private fun openFile(file: File) {
        // Open in code editor or viewer
        // Implementation would launch CodeEditorFragment with file content
    }
}
