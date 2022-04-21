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
  const [confirm, setConfirm] = useState(false);

  var {listingTitle, price, itemCondition, description } = formData;
  var [user, setUser] = useState({});


  // Part of drop down menu for item condition
  let itemConditions = [
    { label: "BRAND NEW", value: "BRAND_NEW" },
    { label: "OPENED", value: "OPENED" },
    { label: "USED", value: "USED" },
    {label: "DAMAGED", value: "DAMAGED"},
    {label: "BROKEN", value: "BROKEN"}
  ]

  let [itemConditionDropDown, setItemConditionDropDown] = useState("UNDEFINED")

  let handleItemConditionChange = (e) => {
    setItemConditionDropDown(e.target.value)
  }


  useEffect(() => {
    if (cookie.get("user")) {
      user = JSON.parse(cookie.get("user"));
      setUser(user);
    } else {
      navigate("/signin");
    }
  }, []);

  function formInputs(event) {
    event.preventDefault();
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
      setFormData({...formData, itemCondition: itemConditionDropDown});

      // Create listing
      var res1 = await axios.post(
          getGlobalState("backendDomain") + "/api/itemListings/newItemListing/" + user.id,
          formData);


      // 2nd Post to upload image to S3
      if(imageFile !== null){
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
      }

      // console.log(JSON.stringify(image.files[0]))

      navigate("/");
    } catch (resError) {
      setError(resError.response.data.error);
    }
  }

  function setConfirmPost(){
    var confirm = true;
    setFormData({...formData, itemCondition: itemConditionDropDown});
    setConfirm(confirm);
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


        <div className="mb-6">
          <label className="block text-grey-darker text-sm font-bold mb-2">
            itemCondition
          </label>
          <select onChange={handleItemConditionChange}>
            <option value="UNDEFINED"> -- Select your item's condition -- </option>
            {
              itemConditions.map((itemConditionDropDown) => <option value={itemConditionDropDown.value}>{itemConditionDropDown.label}</option>)
            }
          </select>
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
          <button className="text-blue-400 font-bold" onClick={() => setConfirmPost()}>Confirm</button>
        </div>
        {confirm === true? (
            <div className="mt-5 flex items-center justify-between">
              <button
                  className="bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded"
                  type="submit"
              >
                Create listing
              </button>
            </div>
        ): (<div></div>)
        }


        <h1 className="mt-5 text-red-500">{error}</h1>
      </form>
  );
}
