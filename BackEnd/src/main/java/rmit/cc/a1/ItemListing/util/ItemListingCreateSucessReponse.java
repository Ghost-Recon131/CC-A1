package rmit.cc.a1.ItemListing.util;

public class ItemListingCreateSucessReponse {

    private boolean successful;

    private long id;

    public ItemListingCreateSucessReponse(boolean successful, Long id) {
        this.successful = successful;
        this.id = id;
    }

    @Override
    public String toString() {
        return "JWTLoginSucessReponse{" +
                "success=" + successful +
                ", itemListingID='" + id + '\'' +
                '}';
    }

}
