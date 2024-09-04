const fs = require("fs-extra");
const concat = require("concat");
build = async () => {
  const files = [
    "./dist/footer-element/runtime.js",
    "./dist/footer-element/polyfills.js",
    "./dist/footer-element/main.js",
  ];
  await fs.ensureDir("customelements");
  await concat(files, "customelements/footer-element.js");
};
build();
