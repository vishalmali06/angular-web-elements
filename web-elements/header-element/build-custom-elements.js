const fs = require("fs-extra");
const concat = require("concat");
build = async () => {
  const files = [
    "./dist/header-element/runtime.js",
    "./dist/header-element/polyfills.js",
    "./dist/header-element/main.js",
  ];
  await fs.ensureDir("customelements");
  await concat(files, "customelements/header-element.js");
};
build();
