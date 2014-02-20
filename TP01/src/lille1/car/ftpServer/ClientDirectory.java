package lille1.car.ftpServer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * 
 * 
 * Classe servant a representer le repertoir courant d'un client
 * Fournit des methodes utiles sur ce repertoir comme listDirectory permettant d'en connaitre le contenu
 *
 */
public class ClientDirectory {

	private File currentFile;

	/**
	 * 
	 * Cree un repertoire a partir du chemin donne
	 * 
	 * @param path le chemin du repertoir courant
	 */
	public ClientDirectory(String path) {
		currentFile = new File(path);
	}

	/**
	 * 
	 * Parcours le dossier courant et liste tous les fichiers et dossiers (non recursif)
	 * 
	 * @return la liste des fichiers du repertoir au format EPLF
	 */
	public String listDirectory() {
		File[] listOfFiles = currentFile.listFiles();
		String result = "";
		System.out.println("CURRENT : " + currentFile.getAbsolutePath());
		if (listOfFiles == null) {
		} else {
			String currentFile = "";
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					currentFile = "+s" + listOfFiles[i].length() + ",m"
							+ listOfFiles[i].lastModified() / 1000 + ",\011"
							+ listOfFiles[i].getName() + "\015\012";
				} else if (listOfFiles[i].isDirectory()) {
					currentFile = "+/,m" + listOfFiles[i].lastModified() / 1000
							+ ",\011" + listOfFiles[i].getName() + "\015\012";
				}
				result += currentFile + "\n";
			}
		}
		return result;
	}

	/**
	 * 
	 * Donne le chemin absolue du repertoir courant sous forme de chaine
	 * 
	 * @return la chaine contenant ce chemin
	 */
	public String getAbsolutePath() {
		System.out.println("CURRENT : " + currentFile.getAbsolutePath());
		return currentFile.getAbsolutePath();
	}

	/**
	 * 
	 * Change le repertoir courant
	 * 
	 * @param path le chemin du nouveau repertoir
	 * @throws IOException 
	 */
	public void setNewPath(String path) throws IOException {
		String newP = this.getRealPath(path);
		System.out.println("Retourne : "+newP);
		currentFile = new File(newP).getCanonicalFile();
		System.out.println("CURRENT : " + currentFile.getAbsolutePath());
	}

	/**
	 * Definit le repertoir parent comme nouveau repertoir courant
	 * (semblable a "cd ..")
	 */
	public void setToParent() {
		System.out.println("TEST entre");
		currentFile = new File(currentFile.getParent());
		System.out.println("CURRENT : " + currentFile.getAbsolutePath());
		System.out.println("TEST sors");
	}

	/**
	 * 
	 * Donne le chemin reel du fichier donne a partir du repertoir courant
	 * Si le chemin donne est absolu, retourne ce chemin
	 * Sinon concatene les chemins et revoit le chemin exact
	 * 
	 * @param filepath le chemin a resoudre
	 * @return le chemin reel
	 */
	public String getRealPath(String filepath) {
		File tmp = new File(filepath);
		if (tmp.isAbsolute()) {
			return filepath;
		} else {
			Path p = Paths.get(currentFile.getAbsolutePath());
			return p.resolve(filepath).toString();
		}
	}
}