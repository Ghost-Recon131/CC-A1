import { getGlobalState, setGlobalState } from "utils/globalState";
import cookie from "js-cookie";
import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, Link, useParams } from "react-router-dom";

export default function Component() {
  var navigate = useNavigate();
  var [image, setImage] = useState("");
  var [error, setError] = useState("");
  var [formData, setFormData] = useState({
    listingtitle: "",
    price: "",
    itemcondition: "",
    description: "",
  });
  var { listingtitle, price, itemcondition, description } = formData;

  var user = {};
  if (cookie.get("user")) {
    user = JSON.parse(cookie.get("user"));
  } else {
    navigate("/signin");
  }

  //   useEffect(() => {
  //   }, []);

  function formInputs(event) {
    event.preventDefault();
    var { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  }
  
  function formImage(event) {
    event.preventDefault();
    setImage({ ...formData, image: event.target });
    // e.target.files, e.target.value
  }

  async function formSubmit(event) {
    event.preventDefault();
    try {
      setFormData({ ...formData, currentUserID: user.id });
      // Create listing
      var res1 = await axios.post(
        getGlobalState("backendDomain") + "/api/itemListings/newItemListing",
        formData
      );

      console.log(JSON.stringify("Response from posting new item listing" + res1.data));

      // TODO: Upload image to S3 - need fix
      var res2 = await axios.post(
          // addImageToListing/{listing ID}?userId={currentUserID}&filename={original name of the uploaded file}
        getGlobalState("backendDomain") + "/api/itemListings/addImageToListing/"+ res1.data + "?userId=" + user.id+ "&filename=" + image.value,
        image.files
      );

      console.log(getGlobalState("Sent to URL" +  "/api/itemListings/addImageToListing/"+ res1.data + "?userId=" + user.id+ "&filename=" + image.value));


      navigate("/");
    } catch (resError) {
      setError(resError.response.data.error);
    }
  }

  return (
    <form
      onSubmit={formSubmit}
      className="bg-white text-black shadow-md rounded px-96 pt-6 pb-8 mb-4 flex flex-col"
    >
      <h1 className="text-3xl font-bold mb-10">Create new item listing</h1>
      <div className="mb-4">
        <label className="block text-grey-darker text-sm font-bold mb-2">
          Listing Title
        </label>
        <input
          value={listingtitle}
          name="listingtitle"
          onChange={formInputs}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
          type="text"
          placeholder="intresting title"
          required
        />
      </div>
      <div className="mb-6">
        <label className="block text-grey-darker text-sm font-bold mb-2">
          price
        </label>
        <input
          value={price}
          name="price"
          onChange={formInputs}
          className="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"
          type="text"
          placeholder="42"
          required
        />
      </div>

      {/* TODO: Change to dropdown */}
      <div className="mb-6">
        <label className="block text-grey-darker text-sm font-bold mb-2">
          itemcondition
        </label>
        <input
          value={itemcondition}
          name="itemcondition"
          onChange={formInputs}
          className="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"
          type="text"
          placeholder="brand new? used?"
          required
        />
      </div>

      <div className="mb-6">
        <label className="block text-grey-darker text-sm font-bold mb-2">
          description
        </label>
        <input
          value={description}
          name="description"
          onChange={formInputs}
          className="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"
          type="text"
          placeholder="tell us about this item"
          required
        />
      </div>
      <input
        type="file"
        id="image-input"
        accept="image/jpeg, image/png, image/jpg"
        onChange={formImage}
      ></input>

      <div className="mt-5 flex items-center justify-between">
        <button
          className="bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded"
          type="submit"
        >
          Create listing
        </button>
      </div>
      <h1 className="mt-5 text-red-500">{error}</h1>
    </form>
  );
}
