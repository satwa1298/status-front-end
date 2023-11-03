//YOUR PASSPHRASE BETWEEN THE QUOTES
const passphrase = "<passphrase>";

//EMAIL FOR USER WHOSE STATUS YOU WANT TO GET
const email = "<email>"; 

//NAME OF FRIEND YOU'RE GETTING STATUS FOR
const friendName = "<name>";



if (config.runsInWidget) {
  const widget = await createWidget();
  Script.setWidget(widget);
  Script.complete();
} else {
  const widget = await createWidget();
  await widget.presentMedium();
}


// Function to fetch user status from API
async function fetchUserStatus(passphrase, email) {
  const url = `<url>/widget/get-status?email=${email}`;
  const req = new Request(url);

  // Add Passphrase Authentication header
  req.headers = {
    "Authorization": `${passphrase}`
  };

  const json = await req.loadJSON();
  console.log(json);
  return json; 
}

// Function to create the widget
async function createWidget() {
  const widget = new ListWidget();
  widget.backgroundColor = new Color("#1a1a1a"); // Dark background

  // Display the friend's name at the top
  const friendNameText = widget.addText(friendName);
  friendNameText.textColor = new Color("#ffffff");
  friendNameText.font = Font.mediumSystemFont(24);  // Increased font size

  widget.addSpacer(16);  // Add spacing between name and status

  const userStatus = await fetchUserStatus(passphrase, email);

  if (userStatus) {
    // Determine appropriate emoji for availability
    let availabilityEmoji = "";
    switch (userStatus.availability) {
      case "Available":
        availabilityEmoji = "🟢";
        break;
      case "Away":
        availabilityEmoji = "🟡";
        break;
      case "Busy":
        availabilityEmoji = "🔴";
        break;
      default:
        availabilityEmoji = "❓";
    }

    // Add some spacing between friendName and the status
    widget.addSpacer(8);

    // Display the availability status emoji and message on the same line
    const combinedText = widget.addText(`${availabilityEmoji}  ${userStatus.message}`);
    combinedText.textColor = new Color("#ffffff");
    combinedText.font = Font.mediumSystemFont(20);

  } else {
    const errorText = widget.addText("Failed to load data");
    errorText.textColor = new Color("#ff0000");
    errorText.font = Font.mediumSystemFont(16);
  }

  return widget;
}