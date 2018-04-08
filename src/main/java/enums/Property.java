package enums;

public enum Property
{
    BOXIMAGE(DataType.FILE),
    BOXDESTINATIONIMAGE(DataType.FILE),
    BOXONFINISHIMAGE(DataType.FILE),
    MEADOWIMAGE(DataType.FILE),
    PLAYERIMAGE(DataType.FILE),
    WALLIMAGE(DataType.FILE),
    NOTHINGIMAGE(DataType.FILE);

    private DataType dataType;

    Property(DataType dataType)
    {
        this.dataType = dataType;
    }

    public DataType getDataType()
    {
        return dataType;
    }
}
