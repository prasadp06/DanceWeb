#!/bin/sh

cd /mnt/share/mp4/upload/test/
echo $1 $2 $3

begin=`date +%s`
start=`date +%s`
echo $time
rm nchall* naccept_chall* merge* omerge* amerge*

ffmpeg -loglevel error -i $1 -s 160x90 -vcodec copy -an n$1

end=`date +%s`
echo "End of remove audio $((end-start)) sec"
start=`date +%s`

ffmpeg -loglevel error -i $2 -vcodec copy -an n$2

end=`date +%s`
echo "End of remove audio $((end-start)) sec "
start=`date +%s`

#ffmpeg -loglevel error -i n$1 -i n$2 -filter_complex '[0:v]pad=iw*2:ih[int];[int][1:v]overlay=W/2:0[vid]' -map [vid] -c:v libx264 -crf 23 -preset veryfast merge$1
ffmpeg -loglevel error -i n$1 -i n$2 -filter_complex '[0:v]pad=iw*2:ih[int];[int][1:v]overlay=W/2:0[vid]' -map [vid] -c:v libx264 -crf 23 merge$1

end=`date +%s`
echo "End of Merge $((end-start)) sec "
start=`date +%s`
ffmpeg -loglevel error -i merge$1 -i $3 -c:v copy -c:a aac -strict experimental -movflags +faststart -preset ultrafast omerge$1

end=`date +%s`
echo "End of add final Audio $((end-start))"
start=`date +%s`
#ffmpeg -loglevel error -i amerge$1 -preset slow -profile:v baseline -movflags +faststart omerge$1 

end=`date +%s`
echo $((end-start))
start=`date +%s`
echo "Completed !! Process took $((end-begin))"
