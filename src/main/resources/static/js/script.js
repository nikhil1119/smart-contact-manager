console.log("Script loaded");


//change theme work
let currentTheme = getTheme();
//intial
document.addEventListener('DOMContentLoaded', ()=>{
	changeTheme();
})

//to-do
function changeTheme(){
    //set to web page
	changePageTheme(currentTheme,currentTheme);

    //set the listener to change theme button
    const changeThemeButton = document.querySelector("#theme_change_button");

    //change text of button
	changeThemeButton.querySelector("span").textContent = 
	currentTheme=="light" ? "Dark" : "Light";

    changeThemeButton.addEventListener("click", (event)=>{
        const oldTheme = currentTheme;
        console.log("theme button clicked");
        if(currentTheme == "dark"){
            currentTheme = "light";
        }
        else{
            currentTheme = "dark";
        }
        changePageTheme(currentTheme,oldTheme);
    })
}

//set theme to local storage
function setTheme(theme){
    localStorage.setItem("theme", theme);
}

//get theme from local storage
function getTheme(){
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}

//change current page theme
function changePageTheme(theme,oldTheme){
	//update in localStorage
	setTheme(currentTheme);

	//remove the old theme
	document.querySelector("html").classList.remove(oldTheme);

	//set the current theme
	document.querySelector("html").classList.add(theme);

	//change text of button
	document.querySelector("#theme_change_button").querySelector("span").textContent = 
	theme=="light" ? "Dark" : "Light";
}
//end of change theme work