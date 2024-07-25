package Classes;

/**
 *
 * @author M AYAN LAPTOP
 */
public class Item {
    private String name;
    private String category;
    private  String id;
    private int singleCostPrice;
    private int singleSalePrice=0;
    private int smallSalePrice=0;
    private int mediumSalePrice=0;
    private int largeSalePrice=0;
    private int xLargeSalePrice=0;
    private int smallCostPrice=0;
    private int mediumCostPrice=0;
    private int largeCostPrice=0;
    private int xLargeCostPrice=0;
    private boolean sizeFlag;
    private boolean sFlag=false;
    private boolean mFlag=false;
    private boolean lFlag=false;
    private boolean xlFlag=false;
    
    public static final String IMAGEPATH=System.getProperty("user.dir").concat("\\Pictures\\");
    private String imageName="---";
    
    protected Item(){
        
    }
    
    protected void setItemId(String id){
        this.id=id;
    }
    
    public Item(String id, String name, String category, int costPrice, boolean sizeVar){
        this.id=id;
        this.name=name;
        this.category=category;
        this.singleCostPrice=costPrice;
        this.sizeFlag=sizeVar;
    }
    
    public Item(String id, String name, String category, boolean sizeVar,int smallCostPrice, int smallSalePrice,int mediumCostPrice, int mediumSalePrice, int largeCostPrice, int largeSalePrice,int xLargeCostPrice, int xLargeSalePrice,int singleCostPrice, int singleSalePrice, String imageName){
        this.id=id;
        this.name=name;
        this.category=category;
        this.singleCostPrice=singleCostPrice;
        this.sizeFlag=sizeVar;
        this.smallCostPrice=smallCostPrice;
        this.smallSalePrice=smallSalePrice;
        this.mediumCostPrice=mediumCostPrice;
        this.mediumSalePrice=mediumSalePrice;
        this.largeCostPrice=largeCostPrice;
        this.largeSalePrice=largeSalePrice;
        this.xLargeCostPrice=xLargeCostPrice;
        this.xLargeSalePrice=xLargeSalePrice;
        this.singleSalePrice=singleSalePrice;
        this.imageName=imageName;
        setFlags();
    }
    
    public Item(String id, String name, String category, boolean sizeVar,int smallCostPrice, int smallSalePrice,int mediumCostPrice, int mediumSalePrice, int largeCostPrice, int largeSalePrice,int xLargeCostPrice, int xLargeSalePrice,int singleCostPrice, int singleSalePrice){
        this.id=id;
        this.name=name;
        this.category=category;
        this.singleCostPrice=singleCostPrice;
        this.sizeFlag=sizeVar;
        this.smallCostPrice=smallCostPrice;
        this.smallSalePrice=smallSalePrice;
        this.mediumCostPrice=mediumCostPrice;
        this.mediumSalePrice=mediumSalePrice;
        this.largeCostPrice=largeCostPrice;
        this.largeSalePrice=largeSalePrice;
        this.xLargeCostPrice=xLargeCostPrice;
        this.xLargeSalePrice=xLargeSalePrice;
        this.singleSalePrice=singleSalePrice;
        setFlags();
    }
    
    public Item(Item item){
        this.id=item.id;
        this.name=item.name;
        this.category=item.category;
        this.singleCostPrice=item.singleCostPrice;
        this.sizeFlag=item.sizeFlag;
        this.smallSalePrice=item.smallSalePrice;
        this.mediumSalePrice=item.mediumSalePrice;
        this.largeSalePrice=item.largeSalePrice;
        this.xLargeSalePrice=item.xLargeSalePrice;
        this.singleSalePrice=item.singleSalePrice;
        this.smallCostPrice=item.smallCostPrice;
        this.mediumCostPrice=item.mediumCostPrice;
        this.largeCostPrice=item.largeCostPrice;
        this.xLargeCostPrice=item.xLargeCostPrice;
        this.imageName=item.imageName;
        setFlags();
    }
    
    private void setFlags(){
        if(sizeFlag){
            if(smallSalePrice>0){
                sFlag=true;
            }
            if(mediumSalePrice>0){
                mFlag=true;
            }
            if(largeSalePrice>0){
                lFlag=true;
            }
            if(xLargeSalePrice>0){
                xlFlag=true;
            }
        }
    }    
    
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public int getSingleCostPrice() {
        return singleCostPrice;
    }

    public int getSingleSalePrice() {
        return singleSalePrice;
    }

    public int getSmallSalePrice() {
        return smallSalePrice;
    }

    public int getMediumSalePrice() {
        return mediumSalePrice;
    }

    public int getLargeSalePrice() {
        return largeSalePrice;
    }

    public int getXLargeSalePrice() {
        return xLargeSalePrice;
    }

    public boolean isSizeFlag() {
        return sizeFlag;
    }

    public boolean issFlag() {
        return sFlag;
    }

    public boolean ismFlag() {
        return mFlag;
    }

    public boolean islFlag() {
        return lFlag;
    }

    public boolean isXlFlag() {
        return xlFlag;
    }

    public void setSingleSalePrice(int singleSalePrice) {
        this.singleSalePrice = singleSalePrice;
    }

    public void setSmallSalePrice(int smallPrice) {
        this.smallSalePrice = smallPrice;
    }

    public void setMediumSalePrice(int mediumPrice) {
        this.mediumSalePrice = mediumPrice;
    }

    public void setLargeSalePrice(int largePrice) {
        this.largeSalePrice = largePrice;
    }

    public void setXLargeSalePrice(int xlPrice) {
        this.xLargeSalePrice = xlPrice;
    }

    public void setSizeFlag(boolean sizeFlag) {
        this.sizeFlag = sizeFlag;
    }

    public void setsFlag(boolean sFlag) {
        this.sFlag = sFlag;
    }

    public void setmFlag(boolean mFlag) {
        this.mFlag = mFlag;
    }

    public void setlFlag(boolean lFlag) {
        this.lFlag = lFlag;
    }

    public void setXlFlag(boolean xlFlag) {
        this.xlFlag = xlFlag;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSingleCostPrice(int costPrice) {
        this.singleCostPrice = costPrice;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public int getSmallCost() {
        return smallCostPrice;
    }

    public void setSmallCost(int sCostPrice) {
        this.smallCostPrice = sCostPrice;
    }

    public int getMediumCost() {
        return mediumCostPrice;
    }

    public void setMediumCost(int mCostPrice) {
        this.mediumCostPrice = mCostPrice;
    }

    public int getLargeCost() {
        return largeCostPrice;
    }

    public void setLargeCost(int lCostPrice) {
        this.largeCostPrice = lCostPrice;
    }

    public int getXLargeCost() {
        return xLargeCostPrice;
    }

    public void setXLargeCost(int xlCostPrice) {
        this.xLargeCostPrice = xlCostPrice;
    }
    
    
    
}
