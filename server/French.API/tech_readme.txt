======================================================
Navigo
======================================================

Folder structure
----------------
[Content]
	[temp]
		*** All temp files will be stored here ***
    [Trips]
        [000001].jpg
        [000002].jpg
		[000003].jpg
		...
		[000001].jpg		
		...
    [Drivers]
        [000001]_[DATE].jpg
        [000002]_[DATE].jpg
		...
	[Cars]
		[000001]_[DATE].jpg
        [000002]_[DATE].jpg
		...
    [Places]
        [Desc]                
            ...
        [Images]
			[000001]
                [GUID].jpg     // media 1 bracnh 1 left
                [GUID]_t.jpg
                [GUID].jpg     // media 1 bracnh 1 center
                [GUID]_t.jpg
                [GUID].jpg     // media 1 bracnh 1 right
                [GUID]_t.jpg          
			[000002]
                [GUID].jpg     // media 2 bracnh 1 left
                [GUID]_t.jpg
                [GUID].jpg     // media 2 bracnh 1 center
                [GUID]_t.jpg
                [GUID].jpg     // media 2 bracnh 1 right
                [GUID]_t.jpg
			[000003]
				[GUID].jpg     // media 3 bracnh 1 left
                [GUID]_t.jpg                
                [GUID].jpg     // media 3 bracnh 1 center
                [GUID]_t.jpg                
                [GUID].jpg     // media 3 bracnh 1 right
                [GUID]_t.jpg                
                [GUID].jpg     // media 3 bracnh 2 left
                [GUID]_t.jpg
                [GUID].jpg     // media 3 bracnh 2 center
                [GUID]_t.jpg
                [GUID].jpg     // media 3 bracnh 2 right
                [GUID]_t.jpg
			...
        [Reports]
			...
----------------


Operation process (done manually by IT support guy, add to the tech manual)
-------------------------------
1. When 30 days passsed
	[Desc] > [MSM0001]
	[Images] > [MSM0001]
	[Reports] > [MSM0001]
2. Backup
	Database (backup)
	physical folder in IIS including NAS
	Folders "Content"
3. Restore
	Follow the deployment process
	Copy the content folder
	Setup file permission