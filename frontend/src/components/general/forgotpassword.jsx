import { getGlobalState, setGlobalState } from "utils/globalState";
import axios from "axios";
import { useState } from "react";
import cookie from 'js-cookie'
import {
    useNavigate,
} from 'react-router-dom';


export default function Component() {
    var [formData, setFormData] = useState({ username: "", secretQuestion: "", secretQuestionAnswer: "" , newPassword: "", confirmNewPassword: ""});
    var { username, secretQuestion, secretQuestionAnswer, newPassword, confirmNewPassword } = formData;
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
            var res = await axios.post(
                getGlobalState("backendDomain") + "/api/RegisterLogin/resetForgottenPassword" + "?username=" + username,
                formData
            );
            console.log(JSON.stringify(res.data));
            if (res.data.error) {
                setError(res.data.error)
                return
            }

            setError("Password reset successfully");
        } catch (resError) {
            setError(resError.response.data.error)
        }
    }

    return (
        <form
            onSubmit={submit}
            className="bg-white text-black shadow-md rounded px-20 pt-6 pb-8 mb-4 flex flex-col"
        >
            <h1 className="text-3xl font-bold mb-10">Forgot Password</h1>
            <div className="mb-4">
                <label className="block text-grey-darker text-sm font-bold mb-2">
                    Account Email
                </label>
                <input
                    value={username}
                    name="username"
                    onChange={formInputs}
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
                    type="text"
                    placeholder="someone@gmail.com"
                    required
                />
            </div>
            <div className="mb-4">
                <label className="block text-grey-darker text-sm font-bold mb-2">
                    Secret Question
                </label>
                <input
                    value={secretQuestion}
                    name="secretQuestion"
                    onChange={formInputs}
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
                    type="text"
                    placeholder="What is... "
                    required
                />
            </div>
            <div className="mb-6">
                <label className="block text-grey-darker text-sm font-bold mb-2">
                secretQuestionAnswer
                </label>
                <input
                    value={secretQuestionAnswer}
                    name="secretQuestionAnswer"
                    onChange={formInputs}
                    className="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"
                    type="password"
                    placeholder="secret Question Answer"
                    required
                />
            </div>
            <div className="mb-6">
                <label className="block text-grey-darker text-sm font-bold mb-2">
                    newPassword
                </label>
                <input
                    value={newPassword}
                    name="newPassword"
                    onChange={formInputs}
                    className="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"
                    type="password"
                    placeholder="your new password"
                    required
                />
            </div>
            <div className="mb-6">
                <label className="block text-grey-darker text-sm font-bold mb-2">
                    confirmNewPassword
                </label>
                <input
                    value={confirmNewPassword}
                    name="confirmNewPassword"
                    onChange={formInputs}
                    className="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"
                    type="password"
                    placeholder="Confirm your new password"
                    required
                />
            </div>

            <div className="flex items-center justify-between">
                <button
                    className="bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded"
                    type="submit"
                >
                    Reset Password
                </button>
            </div>
            <h1 className="mt-5 text-red-500">{error}</h1>
        </form>
    );
}
