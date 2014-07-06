锘縮tart copyright

NAME:
	TextTools
FUNCTION:
	鏂囨湰澶勭悊宸ュ叿
VERAION:
	1.0
AUTHOR:
	jiangxin
Blog:
	http://blog.csdn.net/jiangxinnju
Git:
	https://github.com/jiangxincode
Email:
	jiangxinnju@163.com

Copyright (c) 2014, jiangxin


All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
Neither the name of the jiangxin nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

end copyright
start usage
		Usage: convert [option] filename1 [filename2...]
			-f The encoder of your file.If you don't know the econder,we will try to detect auto.However we can't ensure the validity!
			-t The encoder you want to convert
			-dos2unix Convert your file from Dos to Unix.Can't use with -unix2dos!
			-unix2dos Convert your file from Unix to Dos.Can't use with -dos2unix!
			-b Backup your file when Convert.It's recommended!
			-r Recovey your file..Can't use with other options!
			
			-h Display this usage.
			filename1[,filename2...] The file that you want to convert,at most one file.
			 
		For example:
			convert -f GBK -t UTF-8 test.txt
			convert -dos2unix test.txt
			convert -r test.txt
end usage

start log

	Version 0.01 鎼缓璧峰熀鏈鏋讹紝瀹炵幇鍩烘湰鍔熻兘锛屽寘鎷細
		澶囦唤銆佹仮澶嶆枃浠�
		鍩烘湰缂栫爜杞崲
		鏄剧ず甯姪鏂囨。
		鏂囨湰缂栫爜妫�祴
		DOS/Unix鏂囦欢鏍煎紡浜掕浆
	Version 0.02 涓烘瘡涓簮鏂囦欢娣诲姞浜嗚鏄庝俊鎭紝鍘婚櫎浜嗛儴鍒嗗啑浣欎唬鐮侊紝鎻愰珮浜嗙▼搴忕殑鍙鎬с�
	Version 0.03 涓篎ileProcess绫绘坊鍔犱簡鍒犻櫎鐩綍鐨勫嚱鏁帮紝渚夸簬浠ュ悗鎵╁睍銆�
	Version 0.04 瀹炵幇浠ｇ爜缁熻鍔熻兘銆�
	Version 0.05 涓烘瘡涓簮鏂囦欢娣诲姞浜嗚鏄庝俊鎭紝鍘婚櫎浜嗛儴鍒嗗啑浣欎唬鐮侊紝鎻愰珮浜嗙▼搴忕殑鍙鎬с�
	Version 0.06 瀹炵幇绠�崟鐨勬枃浠堕噸鍛藉悕鍔熻兘銆�
	Version 0.07 Apache commons-io鍖呯殑渚濊禆锛屼娇鐢╦ava鑷甫鐨勭被搴撲唬鏇匡紝鍚屾椂缁熶竴浜嗗嚱鏁版帴鍙ｃ�鎻愰珮浜嗙郴缁熸晥鐜囥�浼樺寲浜嗙▼搴忕粨鏋勩�
	Version 0.08 杩欐槸涓�杈冧负閲嶅ぇ鐨勫崌绾э紝涓昏鍙樺姩涓猴細
		灏嗕笉鍚屾搷浣滅郴缁熶箣闂存枃浠舵牸寮忚繘琛岃浆鍙樼殑閫昏緫鍒ゆ柇閮ㄥ垎鐢变富绫昏浆鍏SPatternConvert锛�
		OSPatternConvert绫诲疄鐜颁簡鏇村ソ鍦板皝瑁咃紝鎻愪緵浜嗘洿澶氬嚱鏁板姛鑳斤紝瀹炵幇鎵归噺鏂囦欢杞崲锛岄瞾妫掓�鏇村ソ锛岀郴缁熺粨鏋勬洿鍔犱紭鑹紱
		鍚屾椂淇浜唂ileFilter绫讳腑鐨勯儴鍒哹ug銆�
	Version 0.09 灏嗗浠藉姛鑳戒粠Mail绫讳腑鍒嗙锛屽崟鍒椾负涓�被锛屼互瀹炵幇缁撴瀯浼樺寲鍜屼箣鍚庣殑绋嬪簭鎵╁睍锛涜繘涓�浼樺寲浜哅ain鐨勭粨鏋勩�
	Version 0.10 灏嗘仮澶嶅姛鑳戒粠Mail绫讳腑鍒嗙锛屽崟鍒椾负涓�被锛屼互瀹炵幇缁撴瀯浼樺寲鍜屼箣鍚庣殑绋嬪簭鎵╁睍锛涜繘涓�浼樺寲浜哅ain鐨勭粨鏋勩�
	Version 0.11 浼樺寲浜哅ain绫诲拰OSPatternConvert绫汇�
	Version 0.12 浼樺寲浜哅ain绫诲拰妫�爜杞爜閮ㄥ垎銆傝嚦姝ain閮ㄥ垎浼樺寲澶т綋瀹屾垚锛屽疄鐜颁簡绋嬪簭鐨勫彲璇绘�銆�
	Version 0.13 瀹炵幇浜嗘秷闄ゆ敞閲婂姛鑳姐�浣嗘槸姝ら儴鍒嗙紪鐮佽瘑鍒瓑閮ㄥ垎杩樺瓨鍦ㄤ竴浜沚ug銆傛閮ㄥ垎浠ｇ爜鍙傝�浜嗕竴浜涚綉鍙嬬殑浠ｇ爜銆�
	Version 0.14 鏇存柊浜嗕俊鎭樉绀洪儴鍒嗭紝渚夸簬鎵╁睍绋嬪簭鍔熻兘銆�
	
end log

start future funtions

	澧炲己绯荤粺椴佹鎬�
	缁熶竴鍛藉悕瑙勮寖
	鍒朵綔Javadoc
	鏀瑰杽java娉ㄩ噴鍙栨秷鍔熻兘
	瀹炵幇C/C++/PHP/jsp/html/C#/shell/bat绛夎瑷�殑娉ㄩ噴鍙栨秷
	瀹炵幇word鏂囦欢閾炬帴
	鏀瑰杽缂栫爜杞崲閮ㄥ垎鐨勫姛鑳斤紝灏嗘鐮佸拰杞爜鍔熻兘鍚堝苟
	闅忔満鏂囨湰鐢熸垚
	閲囩敤log4j浠ｆ浛System.out.println(),瀹炵幇鏃ュ織鍖�
	瀹炵幇鍚勭璇█浠ｇ爜鐨勬牸寮忓寲锛屽湪淇濊瘉姝ｇ‘鐨勫墠鎻愪笂銆�
	
end future funtions