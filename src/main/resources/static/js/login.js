const openSignupBtn = document.getElementById('open-signup-btn');
const haveAccount = document.getElementById('have-account');
const login = document.getElementById('login');
const signup = document.getElementById('signup');
const loginForm = document.getElementById('login-form');
const signupForm = document.getElementById('signup-form');
const mainContent = document.getElementById('main-content');
const body = document.getElementsByTagName("BODY")[0];

const notiContainer = document.getElementById('noti-container');
const notiBox = document.getElementById('noti-box');
const notiContent = document.getElementById('noti-content');
const notiBtn = document.getElementById('noti-btn');
const type = document.getElementById('type');

const welcome = document.getElementById('welcome');
const welcomeLoginBtn = document.getElementById('welcome-login-btn');
const welcomeSignupBtn = document.getElementById('welcome-signup-btn');

const bg1 = document.getElementById('bg-1');
const bg2 = document.getElementById('bg-2');

const requireAuth = document.getElementById('require-auth');
const gender = document.getElementById('gender');

const loading = document.getElementById('loading-box');


let pageType = type.innerHTML;
console.log(pageType);

const openLoading = () =>{
  loading.classList.remove('hide-o');
  mainContent.style.overflow = "hidden";
}

const closeLoading = () => {
  loading.classList.add('hide-opacity');
  setTimeout(() => {
    loading.classList.remove('hide-opacity')
      loading.classList.add('hide-o');
  }, 1500);
}

const openWelcomePage = (t) =>{
  mainContent.classList.add('hide-o');
  welcome.classList.remove('hide-o');
  welcome.classList.remove('hide-opacity');
  if(t) window.history.pushState("Welcome to chatApp", "", "/");
  document.title = "Welcome to chatApp";
  bg1.classList.add('show-bg-1');
  bg2.classList.add('show-bg-2');
}

const openSignupForm = (t) =>{
  welcome.classList.add('hide-o');
  welcome.classList.add('hide-opacity');
  mainContent.classList.remove('hide-o');
  openSignupBtn.innerHTML = "Login";
  haveAccount.innerHTML = 'Already have an account?';
  signup.classList.remove('hide-o');
  login.classList.add('hide-o');
  if(t) window.history.pushState("Signup to chatApp","", "/signup");
  document.title = "Signup to chatApp";
  bg1.classList.remove('show-bg-1');
  bg2.classList.add('show-bg-2');
}

const openLoginForm = (t) =>{
  welcome.classList.add('hide-o');
  welcome.classList.add('hide-opacity');
  mainContent.classList.remove('hide-o');
  openSignupBtn.innerHTML = 'Create Account';
  haveAccount.innerHTML = "Don't have an account?";
  login.classList.remove('hide-o');
  signup.classList.add('hide-o');
  if(t) window.history.pushState("Login to chatApp", "", "/login");
  document.title = "Login to chatApp";
  bg1.classList.remove('show-bg-1');
  bg2.classList.add('show-bg-2');
}

const notiMessage = message =>{
  window.scrollTo(0, 0);
  notiContainer.classList.remove('hide-o');
  notiContent.innerHTML = message;
  body.style.overflow = 'hidden';
  setTimeout(() =>{
    notiContainer.classList.add('hide-o');
    notiContent.innerHTML = '';
    body.style.overflow = 'visible';
  }, 3000);
}

const redirect = (path) =>{
  console.log("hiihih");
  notiContainer.classList.add('hide-o');
  switch (path) {
    case 'login':
        openLoginForm(true);
      break;
    case 'signup':
      openSignupForm(true);
      break;
    case '':
      openWelcomePage(true);
      break;
    default:
      window.location.replace(path);
      break;
  }
}

document.onreadystatechange = function() {
    if (document.readyState === "complete") {
      closeLoading();
      switch (pageType) {
        case 'login':
          welcome.classList.add('hide-o');
          mainContent.classList.remove('hide-o');
          openLoginForm();
          window.history.pushState(" ", "Login to chatApp", "/login");
          break;
        case 'signup':
          welcome.classList.add('hide-o');
          mainContent.classList.remove('hide-o');
          openSignupForm();
          break;
        case '403':
          welcome.classList.add('hide-o');
          mainContent.classList.remove('hide-o');
          openLoginForm();
          requireAuth.classList.remove('hide-o');
          window.history.pushState(" ", "Login to chatApp", "/login");
          setTimeout(() =>{
            requireAuth.classList.add('hide-o');
          }, 15000);
          break;
        default:
          openWelcomePage();
        break;
      }
        //
    } else {
        openLoading();
    }
};

window.onpopstate = e =>{
  let path = window.location.pathname;
  switch (path) {
    case "/login":
      openLoginForm(false);
      break;
    case "/signup":
      openSignupForm(false);
      break;
    default:
    openWelcomePage(true);
  }
};

gender.addEventListener('change', () => {

  if(gender.value === "0")
    gender.style.color = "#8C8B8B";
  else gender.style.color = "black";

});

welcomeLoginBtn.addEventListener('click', () =>{
  openLoginForm(true);
  console.log("pathname: "+window.location.pathname);
});

welcomeSignupBtn.addEventListener('click', () =>{
  welcome.classList.add('hide-o');
  mainContent.classList.remove('hide-o');
  openSignupForm(true);
  console.log("pathname: "+window.location.pathname);
});


openSignupBtn.addEventListener('click', () => {

  let label = openSignupBtn.innerHTML;

  if(label === 'Create Account'){
    openSignupForm(true);
  }
  else {
    openLoginForm(true);
  }

}, true);

loginForm.addEventListener('submit', e => {
  openLoading();
  e.preventDefault();

  $.ajax({
       type: "POST",
       url: "/auth",
       data: $('#login-form').serialize(),
       xhrFields: {
         withCredentials: true
       },
       success: function(output, status, res) {
         console.log("testing1: "+res.getResponseHeader("Authorization"));
         console.log("testing2: "+res.getResponseHeader("Date"));
         console.log("testing3: "+res.getResponseHeader("Content-Type"));
         closeLoading();
         notiMessage("Login successfully!");
       },
       error: () =>{
         closeLoading();
         notiMessage("Incorrect email or password!");
       }
    });
});

signupForm.addEventListener('submit', e => {
  openLoading();
  e.preventDefault();

  $.ajax({
    type: "POST",
    url: "https://secret-brook-88276.herokuapp.com/signup",
    data: $('#signup-form').serialize(),
    //contentType: "application/json",
    success: function() {
      closeLoading();
      notiMessage("Signup successfully!");
      setTimeout(() => {
        redirect('login');
      }, 3000);
    },
    error: () =>{
      closeLoading();
      notiMessage("Something wrong. Please try again!");
    }
  });
});
