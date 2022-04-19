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
  var user = {};
  if (cookie.get("user")) {
    user = JSON.parse(cookie.get("user"));
  }

  useEffect(() => {
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

  return (
    <div className="bg-white text-black">
      <h1 className="text-lg">{itemListing.listingTitle}</h1>
      Condition: {itemListing.itemCondition} <br />
      Description: {itemListing.description} <br />${itemListing.price} <br />
      {images.map((image,index) => (
        <div>
          <img src={image} width="" height="6%"></img>
        </div>
      ))}
      {user.username ? (
        <div>
          <button>edit</button> <button>edit</button>{" "}
        </div>
      ) : (
        <div>
          <button>buy</button>
        </div>
      )}
    </div>
  );
}
