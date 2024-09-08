console.log("Contact.js");
const baseURL = "http://localhost:8081";

//set the modal menu element
const viewContactModal = document.getElementById("view_contact_modal");

// options with default values
const options = {
	placement: 'bottom-right',
	backdrop: 'dynamic',
	backdropClasses:
		'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
	closable: true,
	onHide: () => {
		console.log('modal is hidden');
	},
	onShow: () => {
		console.log('modal is shown');
	},
	onToggle: () => {
		console.log('modal has been toggled');
	},
};

// instance options object
const instanceOptions = {
	id: 'view_contact_modal',
	override: true
};

const contactModal = new Modal(viewContactModal, options, instanceOptions);

function openContactModal() {
	contactModal.show();
}

function closeContactModal() {
	contactModal.hide();
}

async function loadContactData(id) {
	//function call to load data
	console.log(id);
	try {
		const data = await (await fetch(`${baseURL}/api/contacts/${id}`)).json();
		console.log(data);
		document.querySelector("#contact_name").innerHTML = data.name;
		document.querySelector("#contact_email").innerHTML = data.email;
		document.querySelector("#contact_image").src = data.picture;
		document.querySelector("#contact_address").innerHTML = data.address;
		document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
		document.querySelector("#contact_about").innerHTML = data.description;
		const contactFavorite = document.querySelector("#contact_favorite");
		if (data.favourite) {
			contactFavorite.innerHTML =
				"<i class='fas fa-star text-yellow-400'>";
		} else {
			contactFavorite.innerHTML = "<i class='fas fa-star text-white-400'>";
		}

		document.querySelector("#contact_website").href = data.websiteLink;
		document.querySelector("#contact_website").innerHTML = data.websiteLink;
		document.querySelector("#contact_linkedIn").href = data.linkedInLink;
		document.querySelector("#contact_linkedIn").innerHTML = data.linkedInLink;
		openContactModal();
	} catch (error) {
		console.log("Error: ", error);
	}
}


function deleteContact(id) {
    // const isDarkMode = window.matchMedia('(prefers-color-scheme: dark)').matches;
	const isDarkMode = getCurrentTheme();
	console.log("isDarkMode",isDarkMode);

    const confirmButtonC = isDarkMode ? "#1d4ed8" : "#3085d6";
    const cancelButtonC = isDarkMode ? "#ef4444" : "#d33";
    const backgroundC= isDarkMode ? "#1f2937" : "#ffffff";
    const textC = isDarkMode ? "#ffffff" : "#1f2937";
    const iconC= isDarkMode ? "#fbbf24" : "#f59e0b";
    const successIconC= isDarkMode ? "#22c55e" : "#4ade80";

    Swal.fire({
        title: "Do you want to delete this contact?",
        text: "You won't be able to revert this!",
        icon: "warning",
        iconColor: iconC,
        background: backgroundC,
        color: textC,
        showCancelButton: true,
        confirmButtonColor: confirmButtonC,
        cancelButtonColor: cancelButtonC,
        confirmButtonText: "Yes, delete it!"
    }).then((result) => {
        if (result.isConfirmed) {
            const url = `${baseURL}/user/contacts/delete/` + id;

            Swal.fire({
                title: "Deleted!",
                text: "Your contact has been deleted.",
                icon: "success",
                iconColor: successIconC,
                background: backgroundC,
                color: textC,
                confirmButtonColor: confirmButtonC,
                timer: 3000,
                timerProgressBar: true,
                willClose: () => {
                    window.location.replace(url);
                }
            });
        }
    });
}



function getCurrentTheme() {
    // Check if an explicit theme is set in localStorage (if you have a custom theme toggle)
    const savedTheme = localStorage.getItem('theme');
    
    if (savedTheme) {
        return savedTheme === 'dark';
    }

    // Otherwise, use the system preference
    return window.matchMedia('(prefers-color-scheme: dark)').matches;
}