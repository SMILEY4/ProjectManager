package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.DocumentationFile;

public class RawDocumentationFile {


	public int id;
	public String name;
	public String text;




	public static RawDocumentationFile toRaw(DocumentationFile docFile) {
		RawDocumentationFile raw = new RawDocumentationFile();
		raw.id = docFile.id.get();
		raw.name = docFile.name.get();
		raw.text = docFile.text.get();
		return raw;
	}




	public static DocumentationFile fromRaw(RawDocumentationFile rawDocFile, Project project) {
		DocumentationFile docFile = new DocumentationFile(rawDocFile.id, rawDocFile.name, project);
		fromRaw(docFile, rawDocFile, project);
		return docFile;
	}




	public static DocumentationFile fromRaw(DocumentationFile docFile, RawDocumentationFile rawDocFile, Project project) {
		docFile.name.set(rawDocFile.name);
		docFile.text.set(rawDocFile.text);
		return docFile;
	}


}
