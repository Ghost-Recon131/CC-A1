import { getGlobalState, setGlobalState } from "utils/globalState";
import cookie from "js-cookie";
import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, Link, useParams } from "react-router-dom";

export default function Component() {
  var navigate = useNavigate();
  var [itemListing, setItemListing] = useState({});
  var [images, setImages] = useState([]);
  var queryParams = new URLSearchParams(window.location.search);
  var id = queryParams.get("id");
  var [user, setUser] = useState({});

  // For PayPal checkout
  var [formData, setFormData] = useState({ buyerID: "", sellerID: "", itemListingID: "", price: "", currency: "AUD"});

  useEffect(() => {
    console.log("data " + formData);

    if (cookie.get("user")) {
      user = JSON.parse(cookie.get("user"));
      setUser(user);
    }

    // Get item details
    async function fetch1() {
      var res1 = await axios.get(
        getGlobalState("backendDomain") +
          "/api/itemListings/viewListingByID?id=" +
          id
      );
      setItemListing(res1.data);
    }
    fetch1();

    async function fetch2() {
      var res2 = await axios.get(
        getGlobalState("backendDomain") +
          "/api/itemListings/getListingImageLinks/" +
          id
      );
      setImages(res2.data);
    }
    fetch2();
  }, []);


  // Function to delete a listing
  async function deleteListing(){
    await axios.delete(
        getGlobalState("backendDomain") +
        "/api/itemListings/deleteItemListing/" +
        id + "?userID=" + user.id
    );

    // Go back to home after delete
    navigate("/");
  }


  // axios POST for PayPal Checkout
  async function checkOut(){
    setFormData({ buyerID: user.id, sellerID: itemListing.sellerID, itemListingID: itemListing.id, price: itemListing.price, currency: "AUD"});

    // Axios POST
    var status = await axios.post(
        getGlobalState("backendDomain2") + "/api/Transactions/createPayment", formData);

    // go to success or fail page
    navigate("/" + status);
  }

  return (
    <div className="bg-white text-black">
      <h1 className="text-lg">{itemListing.listingTitle}</h1>
      Condition: {itemListing.itemCondition} <br />
      Description: {itemListing.description} <br />${itemListing.price} <br />
      {images.map((image,index) => (
        <div>
          <img src={image} width="10%" height=""></img>
        </div>
      ))}

      {/*TODO: Check userid against itemListing account id*/}
      {user.id ? (

              <div>
                <button className="text-yellow-400" onClick={() => navigate("/editListing?" + itemListing.id)}>Edit Listing</button>
                <p className="text-orange-600">Warning! Deleting process is permanent!</p>
                <button className="text-red-600 font-bold" onClick={() => deleteListing()}>Delete Listing</button>
              </div>
          ):
          (
              <div>
                <button className="text-green-600" onClick={() => checkOut()}>Delete Listing</button>
              </div>
          )
      }
    </div>
  ); // part of return bracket
}
