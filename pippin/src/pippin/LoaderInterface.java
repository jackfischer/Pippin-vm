package pippin;

import java.io.File;
import java.io.FileNotFoundException;

public interface LoaderInterface {
	String load(Memory mem, File file) throws FileNotFoundException;
}