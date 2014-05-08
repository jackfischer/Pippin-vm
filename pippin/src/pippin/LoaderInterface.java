package pippin;

import java.io.File;
import java.io.FileNotFoundException;

public interface LoaderInterface {
	String load(MemoryInterface mem, File file) throws FileNotFoundException;
}