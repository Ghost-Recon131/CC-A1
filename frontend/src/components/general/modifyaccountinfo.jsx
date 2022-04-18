import { getGlobalState, setGlobalState } from "utils/globalState";
import axios from "axios";
import { useState } from "react";
import cookie from 'js-cookie'
import {
    useNavigate,
} from 'react-router-dom';


export default function Component() {
    var [formData, setFormData] = useState({address: "", dob: "", phone: "" });
    var { userID, address, dob , phone} = formData;
    // TODO: get userID variable from cookie
    var [error, setError] = useState("");
    const navigate = useNavigate();

    function formInputs(event) {
        event.preventDefault();
        var { name, value } = event.target;
        setFormData({ ...formData, [name]: value });
    }

    async function submit(event) {
        event.preventDefault();
        console.log(JSON.stringify(formData))
        try {
            var res = await axios.put(
                getGlobalState("backendDomain") + "/api/AccountInfo/updateAccountInfo?userID=" + userID,
                formData
            );
            console.log(JSON.stringify(res.data));
            if(res.data.error){
                setError(res.data.error)
                return
            }
            setError("Account details updated");
        } catch (resError) {
            setError(resError.response.data.error)
        }
    }

    return (
        <form
            onSubmit={submit}
            className="bg-white text-black shadow-md rounded px-20 pt-6 pb-8 mb-4 flex flex-col"
        >
            <h1 className="text-3xl font-bold mb-10">Update Account Details</h1>
            <div className="mb-4">
                <label className="block text-grey-darker text-sm font-bold mb-2">
                    Address
                </label>
                <input
                    value={address}
                    name="address"
                    onChange={formInputs}
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
                    type="text"
                    placeholder="1 Somewhere Street Suburb State 1234"
                    required
                />
            </div>
            <div className="mb-6">
                <label className="block text-grey-darker text-sm font-bold mb-2">
                    DOB
                </label>
                <input
                    value={dob}
                    name="dob"
                    onChange={formInputs}
                    className="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"
                    type="text"
                    placeholder="dob"
                    required
                />
            </div>
            <div className="mb-6">
                <label className="block text-grey-darker text-sm font-bold mb-2">
                    phone
                </label>
                <input
                    value={phone}
                    name="phone"
                    onChange={formInputs}
                    className="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"
                    type="text"
                    placeholder="+61 0412 345 678"
                    required
                />
            </div>
            <div className="flex items-center justify-between">
                <button
                    className="bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded"
                    type="submit"
                >
                    Update Details
                </button>
            </div>
            <h1 className="mt-5 text-red-500">{error}</h1>
        </form>
    );
}
