/**
 * 
 */
 window.addEventListener('DOMContentLoaded', () => {
	
	const formCreate = document.querySelector('#formCreate');
	const btnSave = document.querySelector('#btnSave');
	
	const title = document.querySelector('#title');
	const content = document.querySelector('#content');
	const score = document.querySelector('#score').value;
	const storage = document.querySelector('#storage');
	
	btnSave.addEventListener('click', function () {
		if (title.value == '') {
			alert('제목은 필수사항입니다!');
			return;
		}
		storage.value = 0;
		formCreate.action = '/review/create';
		formCreate.method = 'post';
		formCreate.submit();
		});
		
	const btnRelease = document.querySelector('#btnRelease');
	btnRelease.addEventListener('click', function () {
		if (title.value == '' || content.value == '' || score == '') {
			alert('제목과 내용, 별점을 모두 채워주세요!');
			return;
		}
		formCreate.action = '/review/create';
		formCreate.method = 'post';
		formCreate.submit();
	})

		
	
})