import { getGlobalState, setGlobalState } from "utils/globalState";
import cookie from "js-cookie";
import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, Link, useParams } from "react-router-dom";

export default function Component() {
  var navigate = useNavigate();
  const [imageName, setImageName] = useState("");
  const [imageFile, setImageFile] = useState(null);
  var [error, setError] = useState("");
  var [formData, setFormData] = useState({
    listingTitle: "",
    price: "",
    itemCondition: "",
    description: "",
  });

  var {listingTitle, price, itemCondition, description } = formData;

  var [user, setUser] = useState({});


  useEffect(() => {
    if (cookie.get("user")) {
      user = JSON.parse(cookie.get("user"));
      setUser(user);

      console.log("Get cookie" + cookie.get("user"));
      console.log("Current user" + user.id);

    } else {
      navigate("/signin");
    }
  }, []);

  function formInputs(event) {
    event.preventDefault();
    // console.log(event.target.value);
    var { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  }

  function formImage(event) {
    event.preventDefault();

    let file = event.target.files[0];
    const imageFile = new FormData();
    imageFile.append("file", file);

    setImageFile(imageFile);

    setImageName(
      event.target.value
        .toString()
        .substring(event.target.value.toString().lastIndexOf("\\") + 1)
    );

    // setImageFile(event.target.files[0]);
  }

  async function formSubmit(event) {
    event.preventDefault();
    try {
      setFormData({ ...formData});

      console.log(
          JSON.stringify("userid before 1st post" + user.id)
      );

      // Create listing
      var res1 = await axios.post(
        getGlobalState("backendDomain") + "/api/itemListings/newItemListing/" + user.id,
        formData
      );

      console.log(
        JSON.stringify("Response from posting new item listing" + res1.data)
      );

      // 2nd Post to upload image to S3
      var res2 = await axios.post(
        // addImageToListing/{listing ID}?userId={currentUserID}&filename={original name of the uploaded file}
        getGlobalState("backendDomain") +
          "/api/itemListings/addImageToListing/" +
          res1.data +
          "?userId=" +
          user.id +
          // user.id +
          "&filename=" +
          imageName,
          imageFile
      );

      console.log(
        "/api/itemListings/addImageToListing/" +
          res1.data +
          "?userId=" +
          user.id +
          "&filename=" +
          imageName
      );

      // console.log(JSON.stringify(image.files[0]))

      navigate("/");
    } catch (resError) {
      // setError(resError.response.data.error);
    }
  }

  function itemCondition(event) {
    event.preventDefault();
    if (event.target.value === "BRAND_NEW") {
      setFormData({ ...formData, itemCondition: "BRAND_NEW"});
    } else if (event.target.value === "OPENED") {
      setFormData({ ...formData, itemCondition: "OPENED"});
    }else if (event.target.value === "USED") {
      setFormData({ ...formData, itemCondition: "USED"});
    }else if (event.target.value === "DAMAGED") {
      setFormData({ ...formData, itemCondition: "DAMAGED"});
    }else if (event.target.value === "BROKEN") {
      setFormData({ ...formData, itemCondition: "BROKEN"});
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
          listingTitle
        </label>
        <input
          value={listingTitle}
          name="listingTitle"
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
          itemCondition
        </label>


        <select
            className="custom-select text-capitalize"
            onChange={itemCondition}
            value=""
        >
          <option hidden>{user.username}</option>
          <option value="BRAND_NEW">Brand new</option>
          <option value="OPENED">Opened</option>
          <option value="USED">Used</option>
          <option value="DAMAGED">Damaged</option>
          <option value="BROKEN">Broken</option>
        </select>



        {/*<input*/}
        {/*  value={itemCondition}*/}
        {/*  name="itemCondition"*/}
        {/*  onChange={formInputs}*/}
        {/*  className="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"*/}
        {/*  type="text"*/}
        {/*  placeholder="brand new? used?"*/}
        {/*  required*/}
        {/*/>*/}
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
