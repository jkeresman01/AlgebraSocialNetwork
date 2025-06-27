import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { Provider } from "@/components/common/Provider.jsx";
import App from "./App.jsx";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <Provider>
      <App />
    </Provider>
  </StrictMode>,
);
