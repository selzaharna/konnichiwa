$(function() {

	// swap backgrounds on intro...
	var seconds = 5000;
	var step = 0;
	var limit = 3;

	$(".intro").addClass("bg"+step);

  	setInterval(function(){
    	$(".intro").removeClass("bg"+step);
    	step = (step > limit) ? 0 : step + 1;
    	$(".intro").addClass("bg"+step);
  	},seconds);

  	// play sound on 'get started' button click
  	var audioElement = document.createElement('audio');
		audioElement.setAttribute('src', 'sound/k.mp3');
		$('.get-started').click(function() {
			audioElement.play();
		});

});