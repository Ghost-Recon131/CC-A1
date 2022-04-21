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
  var [confirmDelete, setConfirmDelete] = useState(false);


  useEffect(() => {

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


  // Set
  function confirmDeleteListing(){
    var confirm = true;
    setConfirmDelete(confirm);
  }


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
  async function checkOut() {
    const domain = window.location.host;

    // axios post
    const bodyParameters = {
      buyerID: user.id,
      sellerID: itemListing.accountId,
      itemListingID: itemListing.id,
      price: itemListing.price,
      currency: "AUD",
      successURL: domain + "/transactionsuccess",
      cancelURL: domain + "/transactionfail"
    };

    var status = await axios.post(getGlobalState("backendDomain2") + "/api/Transactions/createPayment", bodyParameters);

    console.log("POST Response: " + JSON.stringify(status))
    console.log("redirect link: " + JSON.stringify(status.data))

    window.open(status.data, '_blank');
  }

  return (
      <div className="bg-white text-black">
        <h1 className="text-lg">{itemListing.listingTitle}</h1>
        Condition: {itemListing.itemCondition} <br />
        Description: {itemListing.description} <br />${itemListing.price} <br />
        {images.map((image,index) => (
            <div>
              <img src={image} width="15%" height=""></img>
            </div>
        ))}


        {/*If: logged-in user == creator of item listing, show below*/}
        {user.id === itemListing.accountId? (
            <div>
              <button className="text-yellow-400" onClick={() => navigate("/editListing?id=" + itemListing.id)}>Edit Listing</button>
              <p className="text-orange-600">Warning! Deleting process is permanent!</p>
              <br></br>
              <button className="text-red-600 font-bold" onClick={() => confirmDeleteListing()}>Delete Listing</button>

              {/* If: logged-in user == creator of item listing and confirmed to delete account */}
              {confirmDelete === true? (
                  <div>
                    <button className="text-red-600 font-bold" onClick={() => deleteListing()}>CONFIRM DELETE</button>
                  </div>
              ): (<div className="text-yellow-500 font-bold"></div>)
              }
            </div>

        ): (<div className="text-yellow-500 font-bold"></div>)
        }


        {/*Checks that a user ID is present*/}
        {user.id != null && user.id !== itemListing.accountId? (
            <div>
              <button className="text-blue-400 font-bold" onClick={() => checkOut()}>PayPal Checkout</button>
            </div>
        ): (<div></div>)
        }

      </div>
  ); // part of return bracket
}
