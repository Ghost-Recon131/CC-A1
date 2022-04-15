import React from "react";
import "index.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { render } from "react-dom";
import Home from "components/general/home";
import SignIn from "components/general/signin";
import NavBar from "components/general/navbar";
import Footer from "components/general/footer";

render(
  <div className="bg-black text-white px-10">
    <BrowserRouter>
      <NavBar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/signin" element={<SignIn />} />
      </Routes>
      <Footer />
    </BrowserRouter>
  </div>,
  document.getElementById("root")
);
