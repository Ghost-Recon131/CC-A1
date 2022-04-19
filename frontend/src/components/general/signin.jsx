import { getGlobalState, setGlobalState } from "utils/globalState";
import axios from "axios";
import { useState } from "react";
import cookie from "js-cookie";
import { useNavigate } from "react-router-dom";

export default function Component() {
  var [formData, setFormData] = useState({ username: "", password: "" });
  var { username, password } = formData;
  var [error, setError] = useState("");
  const navigate = useNavigate();

  function formInputs(event) {
    event.preventDefault();
    var { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  }

  async function formSubmit(event) {
    event.preventDefault();
    console.log(JSON.stringify(formData));
    try {
      var res = await axios.post(
        getGlobalState("backendDomain") + "/api/RegisterLogin/login",
        formData
      );
      // console.log(JSON.stringify(res.data));
      if (res.data.error) {
        setError(res.data.error);
        return;
      }
      // Param 1 = name for cookie, 2nd = value, stringify turns object to string
      // create or update uset set
      // to get cookie, eg :  var user = JSON.parse(cookie.get("user"))
      // eg user.id or user.username
      cookie.set("user", JSON.stringify(res.data));
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
      <h1 className="text-3xl font-bold mb-10">Sign in</h1>
      <div className="mb-4">
        <label className="block text-grey-darker text-sm font-bold mb-2">
          Username
        </label>
        <input
          value={username}
          name="username"
          onChange={formInputs}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
          type="text"
          placeholder="Username"
          required
        />
      </div>
      <div className="mb-6">
        <label className="block text-grey-darker text-sm font-bold mb-2">
          Password
        </label>
        <input
          value={password}
          name="password"
          onChange={formInputs}
          className="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"
          type="password"
          placeholder="Password"
          required
        />
      </div>
      <div className="flex items-center justify-between">
        <button
          className="bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded"
          type="submit"
        >
          Sign In
        </button>
      </div>
      <h1 className="mt-5 text-red-500">{error}</h1>
    </form>
  );
}
