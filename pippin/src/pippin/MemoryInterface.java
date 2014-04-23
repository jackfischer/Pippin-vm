package pippin;

public interface MemoryInterface {

	int CODE_SIZE = 0;
	int DATA_SIZE = 0;

	void setCode(int i, int j);

	void setCode(int index, long lng) throws CodeAccessException;

	void setData(int index, int value) throws DataAccessException;

	int getData(int index) throws DataAccessException;

	long getCode(int index) throws CodeAccessException;

	void clearCode();

	void clearData();

}
