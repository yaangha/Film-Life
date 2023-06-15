/**
 * 
 */
 window.addEventListener('DOMContentLoaded', () => {
	
	const btnChangeRelease = document.querySelector('#btnChangeRelease');
		const btnChangeSave = document.querySelector('#btnChangeSave');
		
		btnChangeRelease.addEventListener('click', () => {
			document.querySelector('#releaseList').style.display = '';	
			document.querySelector('#saveList').style.display = 'none';	
			document.querySelector('#btnChangeSave').style.color = 'silver';
			document.querySelector('#btnChangeRelease').style.color = 'black';		
			document.querySelector('#btnChangeRelease').style.borderBottom = 'thin solid black';	
			document.querySelector('#btnChangeSave').style.borderBottom = 'thin solid silver';		
		})
		
		btnChangeSave.addEventListener('click', () => {
			document.querySelector('#saveList').style.display = '';
			document.querySelector('#releaseList').style.display = 'none';
			document.querySelector('#btnChangeSave').style.color = 'black';
			document.querySelector('#btnChangeRelease').style.color = 'silver';
			document.querySelector('#btnChangeSave').style.borderBottom = 'thin solid black';	
			document.querySelector('#btnChangeRelease').style.borderBottom = 'thin solid silver';	
		})
		
		function deleteUser() {
			const result = confirm('정말 탈퇴하시겠습니까? 탈퇴하시면 작성된 글들은 모두 삭제됩니다.');
			const idName = document.querySelector('#idName').innerText;
			if (result) {
				window.location.href = '/deleteUser?idName=' + idName;
			}
		}
})