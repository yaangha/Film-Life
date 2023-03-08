/**
 * 
 */
 window.addEventListener('DOMContentLoaded', () => {
	
		const formSearch = document.querySelector('#formSearch');
		const btnSearch = document.querySelector('#btnSearch');
		btnSearch.addEventListener('click', () => {
			formSearch.action = '/review/search';
			formSearch.method = 'post';
			formSearch.submit();
		})
		
		const btnSeeAll = document.querySelector('#btnSeeAll');
		const btnSeeSix = document.querySelector('#btnSeeSix');
		
		if (btnSeeAll || btnSeeSix) {
			btnSeeAll.addEventListener('click', () => {
				document.querySelector('#displayNone').style.display = '';
				btnSeeAll.style.display = 'none';
				btnSeeSix.style.display = '';
			})
				
			btnSeeSix.addEventListener('click', () => {
				document.querySelector('#displayNone').style.display = 'none';
				btnSeeAll.style.display = '';
				btnSeeSix.style.display = 'none';
			})
		}
		
})