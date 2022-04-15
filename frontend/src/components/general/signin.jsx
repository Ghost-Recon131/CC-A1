import { getGlobalState, setGlobalState } from "utils/globalState";
import axios from "axios";
import { useState } from "react";

export default function Component() {
  var [formData, setFormData] = useState({ email: "", password: "" });
  var { username, password } = formData;
  var [error, setError] = useState("");

  function formInputs(event) {
    event.preventDefault();
    var { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  }

  async function submit(event) {
    event.preventDefault();
    try {
      var res = await axios.post(
        getGlobalState("backendDomain") + "/api/RegisterLogin/login",
        formData
      );
      if(res.data.success === true){
        setGlobalState("jwt", res.data.token)
      }
      console.log(JSON.stringify(res.data));
    } catch (resError) {}
  }

  return (
    <form
      onSubmit={submit}
      className="bg-white text-black shadow-md rounded px-20 pt-6 pb-8 mb-4 flex flex-col"
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
      <h1 className="text-red-500">{error}</h1>
    </form>
  );
}