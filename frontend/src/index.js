import React from "react";
import "index.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { render } from "react-dom";
import Home from "components/general/home";
import SignIn from "components/general/signin";
import Register from "components/general/register";
import ForgotPassword from "components/general/forgotpassword";
import NavBar from "components/general/navbar";
import Footer from "components/general/footer";
import ModifyAccountInfo from "components/general/modifyaccountinfo";

render(
  <div className="bg-black text-white px-10">
    <BrowserRouter>
      <NavBar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/signin" element={<SignIn />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgotpassword" element={<ForgotPassword />} />
        <Route path="/modifyaccountinfo" element={<ModifyAccountInfo />} />
      </Routes>
      <Footer />
    </BrowserRouter>
  </div>,
  document.getElementById("root")
);
