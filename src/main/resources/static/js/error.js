const container = document.getElementById('container');
const img = document.getElementById('img');
const err = document.getElementById('error');
const mes = document.getElementById('mes');
const errorMes = document.getElementById('error-message');
const login = document.getElementById('login');
const home = document.getElementById('home');
const type = document.getElementById('type');

let errorType = type.innerHTML;
console.log("error type:  "+errorType);

document.onreadystatechange = function() {
    if (document.readyState == "complete") {
      img.classList.add('slide');
      error.classList.remove('hide-o');
      mes.classList.remove('hide-o');
      switch (errorType) {
        case '404':
          contentNotFound();
          break;
        case '403':
          accessDenied();
          break;
        case '500':
          internalError();
          break;
        default:
          someError();
          break;
      }
    }
};

const accessDenied = () =>{
  err.innerHTML = '403';
  errorMes.innerHTML = "Looks like you didn't have permission to access this content.";
  login.classList.remove('hide-d');
  home.classList.remove('hide-d');
}

const contentNotFound = () =>{
  err.innerHTML = '404';
  errorMes.innerHTML = "Uh-oh nothing here......";
  login.classList.add('hide-d');
  home.classList.remove('hide-d');
}

const internalError = () =>{
  err.innerHTML = "500";
  errorMes.innerHTML = 'Oh no, the server is not feeling okay this time! Please come back later.';
  login.classList.add('hide-d');
  home.classList.remove('hide-d');
}

const someError = () =>{
  err.innerHTML = "Opps!";
  errorMes.innerHTML = 'Something went wrong!';
  login.classList.add('hide-d');
  home.classList.remove('hide-d');
}
