/**
 * 
 */
 window.addEventListener('DOMContentLoaded', function () {
	const idNameInput = document.querySelector('#idName');
	const btnJoin = document.querySelector('#btnJoin');
	
	
	idNameInput.addEventListener('change', function () {
		const idName = idNameInput.value;
		axios
		.get('/checkid?idName=' + idName)
		.then(response => { displayCheckResult(response.data) })
		.catch(err => { console.log(err); })
	});
	
	function displayCheckResult(data) {
		const checkIdName = document.querySelector('#checkIdName');
		
		let str = '';
		if (data == 'ok') {
			str += '<div style="color:green; text-align:left;">사용할 수 있는 아이디입니다.</div>';
			checkIdName.innerHTML = str;
		} else {
			str += '<div style="color:red; text-align:left;">사용할 수 없는 아이디입니다.</div>';
			checkIdName.innerHTML = str;
		}
	}
	
	const passwordInput= document.querySelector('#password');
	const pwChkInput = document.querySelector('#pwChk');
	
	pwChk.addEventListener('change', function () {
		password = passwordInput.value;
		pwChk = pwChkInput.value;
		console.log(pwChk);
		console.log(password);
		const checkPw = document.querySelector('#checkPw');
		let str = '';
		if (password == pwChk) {
			str += '<div style="color:green; text-align:left;">비밀번호가 일치합니다.</div>';
			checkPw.innerHTML = str;
		} else {
			str += '<div style="color:red; text-align:left;">비밀번호가 일치하지 않습니다.</div>';
			checkPw.innerHTML = str;
		}
	});
	
	
});