[33mcommit 5a87491e7684044a5de4456a42840d6a03c25b1d[m[33m ([m[1;36mHEAD -> [m[1;32mdevelop[m[33m, [m[1;31morigin/develop[m[33m)[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Mon Mar 11 00:29:05 2019 +0100

    Replace generic ChoiceBoxes with custom ComboBoxes

[33mcommit 2d13e910a308dd347a756ac56622afc3ee25ae6a[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Mar 10 23:31:43 2019 +0100

    Add Delete-Task-function

[33mcommit 37a4c399d87b0b1bca5185a2d8e886b79bd7f3df[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Mar 10 18:16:40 2019 +0100

    Add Create-Task-function

[33mcommit 5e495c836f4e193bf7993cc344f73addbcc2bc3b[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Mar 10 15:53:11 2019 +0100

    Add TaskAttrib: Dependency

[33mcommit e65578faef7d8e21f582aae176c94422462a1245[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Fri Mar 8 16:39:37 2019 +0100

    Resolve problems

[33mcommit bfe8eb2199d3a8795a931677af6a0a9375788cbb[m
Merge: c827ed8 64f2979
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Fri Mar 8 16:34:42 2019 +0100

    Merge branch 'AttribRework' into develop
    
    # Conflicts:
    #       src/com/ruegnerlukas/taskmanager/data/Task.java
    #       src/com/ruegnerlukas/taskmanager/data/filter/FilterCriteria.java
    #       src/com/ruegnerlukas/taskmanager/logic/FilterLogic.java
    #       src/com/ruegnerlukas/taskmanager/logic/Logic.java
    #       src/com/ruegnerlukas/taskmanager/logic/TaskLogic.java
    #       src/com/ruegnerlukas/taskmanager/ui/projectsettingsview/taskattribs/TaskAttributeNode.java

[33mcommit 64f2979a604bff9308bcfff944a17bf1e7984fc7[m[33m ([m[1;32mAttribRework[m[33m)[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Mar 7 19:11:54 2019 +0100

    Reworked Warning when saving TaskAttrib-Changes

[33mcommit 320741471f194bc2c6ee9246383e3968257531b0[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Wed Mar 6 21:28:45 2019 +0100

    Reworked TaskAttribs in ProjectSettings

[33mcommit 5e8a9fd971395276ee777d7b2c04ceeb65f78ed6[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Feb 21 13:41:46 2019 +0100

    Small changes

[33mcommit 4f496f27987db9403b9b2d3402468b698e9c4eb1[m[33m ([m[1;31morigin/AttribRework[m[33m)[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Wed Feb 20 22:45:10 2019 +0100

    Reworked AttributeValue-Validation-System

[33mcommit 172ed2054f9fdbc7f5be94b94f62e245ccb69b9b[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Wed Feb 20 18:35:54 2019 +0100

    Reworked Attribute-Update-System

[33mcommit 1cddc2c07231cc8ee01bdbaabfab968c48e3d12b[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Wed Feb 20 15:14:28 2019 +0100

    Reworked Filter-System

[33mcommit c827ed8293a270a9c237b27d2c4166dfa36dd00a[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Tue Feb 19 22:28:30 2019 +0100

    (WIP)Dependencies

[33mcommit 67da0b49b2242d86e9812971c67937825e1cbf15[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Fri Feb 15 22:46:31 2019 +0100

    Add jump-to-task + breadcrumb in sidebar

[33mcommit 8d3619f9320d6f67cfe8ab4160eb06861086a65a[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Feb 14 21:45:04 2019 +0100

    Small styling/design changes

[33mcommit 25e70752a7ef73ace6efd890ee51216fe678ca9a[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Feb 14 19:05:44 2019 +0100

    Change order of flags in settings

[33mcommit 524a947c7b4b18e541a5aaa5697ca827fed9fecc[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Tue Feb 12 22:19:52 2019 +0100

    Fixed small ui bugs in (ProjectSettings -> TextAttrib)

[33mcommit 1e288bfad86edd209991de0afe1cf51c3c73ae56[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Tue Feb 12 21:24:58 2019 +0100

    Handle NoValues in sidebar

[33mcommit 386dd9ad09913908dcbaa822a8eb2bbb44177d45[m
Merge: c64a78d 288b291
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Tue Feb 12 15:32:55 2019 +0100

    Merge branch 'develop' of https://github.com/SMILEY4/ProjectManager into develop

[33mcommit c64a78d8e9c46a0886769d792afa8bd9b22b2e81[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Tue Feb 12 15:32:27 2019 +0100

    Warning when changing crit. TaskAttribs, Clean up ProjectSettings

[33mcommit a800d38b8e9960df5c45fcb3f1a85562c323c0f0[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sat Feb 9 19:33:56 2019 +0100

    Fixed small bug

[33mcommit 80589553b48d8ae82090fdc892d3639f7a9fd0c5[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sat Feb 9 19:32:32 2019 +0100

    Fixed small bug

[33mcommit 961d7fd1d8f1619f3107a2a8d9f9e9a6e4283451[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sat Feb 9 19:30:59 2019 +0100

    Fixed small bugs in layout

[33mcommit 59c2daa17c52e70905ab6f4030d9e115cc8788f5[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sat Feb 9 19:21:23 2019 +0100

    Added Presets (Filter,Group,Sort)

[33mcommit 288b291432ec599e70742d8e8c1b57b342e4bcb4[m
Author: SMILEY4 <ruegnerlukas@gmail.com>
Date:   Sat Feb 9 18:54:28 2019 +0100

    Create README.md

[33mcommit f3ae6b9b9eb277f66a1f696f7a57539d384d8ca5[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Fri Feb 8 16:52:54 2019 +0100

    Added Save.Load.Reset to Sort-Tasks

[33mcommit c1795932c741795a107e0505fa46766be07796e9[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Fri Feb 8 15:41:14 2019 +0100

    Added Save.Load.Reset to Filter-Tasks

[33mcommit b9040cce06e493fe35676161bc47d6c5f8495d9b[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Fri Feb 8 12:01:45 2019 +0100

    Added Save,Load,Reset to Group-Tasks-UI

[33mcommit 04a2c42853c92ab57b0bcdeebcc9ec869f446fa8[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Feb 7 22:56:48 2019 +0100

    Improved sidebar-behaviour

[33mcommit 7651d4095a6670f91ca7fa912ef3760912d69522[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Feb 7 22:39:11 2019 +0100

    Fixed smaller bugs

[33mcommit e64fbd24a0f7ccbb6344eb24f13634e93035a602[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Feb 7 22:35:15 2019 +0100

    Fixed bug: selected TaskCard not highlighted after List-rebuild

[33mcommit 1836ccac33f841fad8bd0522cb067975604aa4ad[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Feb 7 16:50:54 2019 +0100

    Fixed Bug: no TaskList-refresh when changing some AttribValues

[33mcommit 12cc8690ef3d1e93988cdee53964d5ff5c3d015a[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Feb 7 16:41:31 2019 +0100

    Fixed Task-Sort

[33mcommit 5ddc2fe354f7061a4ae57aded3dace0b05b8047f[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Feb 7 16:01:27 2019 +0100

    Fixed bug: TaskGroup-headerstring null

[33mcommit f174de30127011bc47720286e1206497ddfa393d[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Feb 7 14:30:43 2019 +0100

    Highlight selected task

[33mcommit 8eaa31bf89d5529c4e67182edb7688d6e5eb6f8c[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Feb 7 14:04:10 2019 +0100

    Convert taskcard-fxml to java-code

[33mcommit 8ff2e28d05ff55a2e42737886d181b4c4e2c32ab[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Wed Feb 6 21:30:05 2019 +0100

    Fixed 'Bug': reduced amount of TaskList-rebuilds

[33mcommit 5717b50d836a2237b8b7ca7bdffdf022b72625ab[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Wed Feb 6 15:23:10 2019 +0100

    Fixed bug: TaskCards: listener not deregistered

[33mcommit 87900ba89908e8d61e7b8b3cdbe508fe9dfe63aa[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Wed Feb 6 15:12:22 2019 +0100

    Edit Attribute-values in sidebar

[33mcommit f153557bb08b0607cf6a62d6d0a7e8bc31fd7184[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Wed Feb 6 14:10:47 2019 +0100

    Display Task in Sidebar

[33mcommit abdd27388be042bed1c1961f7b35f00cc6e10586[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Tue Feb 5 21:41:47 2019 +0100

    Add height-control to TextArea of Multiline-TxtAttrib

[33mcommit aaa467b6229ad51279d28a640899b609e2744e88[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Tue Feb 5 19:39:27 2019 +0100

    Reworked/Improved Data-Flow/Task-Grouping

[33mcommit 659cee192f24e3afc14d3ad282660002936dd31d[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Mon Feb 4 20:38:56 2019 +0100

    Fixed small bugs

[33mcommit ab1969b17dd33d15dfa7a43ebec5e5cadd41df16[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Mon Feb 4 19:21:08 2019 +0100

    Added label task-count

[33mcommit e11994814ec7cee04a5ab2d4d050a2a1d4b7d893[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Mon Feb 4 19:05:46 2019 +0100

    Implemented ChoiceListField in ui

[33mcommit 64e00423ecadc5cdcae433c2ff0f0baed1a755f8[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Mon Feb 4 17:45:09 2019 +0100

    Added ChoiceListField

[33mcommit f7887546db512e1791fab556aadf5a9c9662bee5[m
Merge: 6812ec5 b1b10c1
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Feb 3 23:06:59 2019 +0100

    Merge branch 'Rewrite/Improve' into develop

[33mcommit b1b10c1e261c1cd4208301f545d1c843888c115f[m[33m ([m[1;31morigin/Rewrite/Improve[m[33m, [m[1;32mRewrite/Improve[m[33m)[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Feb 3 23:06:35 2019 +0100

    Fixed Bugs

[33mcommit 3b8322dc531b55f2c744329739d349d4f2e196d3[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Feb 3 21:59:57 2019 +0100

    Improved Request/Response

[33mcommit 1886e83a9c9aecc631af6c704a57fafec266d268[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Feb 3 18:32:18 2019 +0100

    Improvements to Request/Response

[33mcommit 75b7bd7f7445ff4159b0be1b4645fa73134ace61[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Feb 3 16:38:07 2019 +0100

    Rework UI-Code

[33mcommit 107ac73c81a6aacd6abd9c894c0421da3eebb679[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Feb 3 14:15:09 2019 +0100

    CleanUp data

[33mcommit 33351036b00248426052b38adf14035415fac1b4[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Feb 3 13:28:56 2019 +0100

    Rework Sort-Logic

[33mcommit 33ed8284beffe99b60a11985f16aeeb353809685[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Feb 3 13:25:47 2019 +0100

    Rework Filter-Logic

[33mcommit aca973dc38de1944c58686b6279b848f2ac00d1f[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Feb 3 13:07:58 2019 +0100

    Rework TaskGroup-Logic

[33mcommit 91103eb82fc3cebd81b2d9868f5d569471bf3256[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sat Feb 2 18:14:16 2019 +0100

    Rework Task-Logic

[33mcommit 183fb15d13a15dcc152373c1cb38190666501429[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sat Feb 2 16:00:50 2019 +0100

    Changes

[33mcommit e2f9e8a16f68e5404e28e7d815255c85189bf83f[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sat Feb 2 14:33:30 2019 +0100

    Rework Attribute-Logic

[33mcommit 1bd1440a2eb5d92b573763e530bcecce9d516cf0[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Fri Feb 1 14:39:46 2019 +0100

    Rework Setup + Project-Logic

[33mcommit 6812ec5db819d00e6b693b302ab258e47c1a8cf8[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Jan 27 17:43:01 2019 +0100

    First Impl of Task-Filter-System

[33mcommit 99837b8d5a8803182005321d0123c6593c54d30f[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Jan 27 15:45:22 2019 +0100

    Documentation

[33mcommit 03f1fc30b6508f2f3952753ee77d5e86295a941d[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Jan 27 11:26:04 2019 +0100

    Sort TaskLists based on GroupBy-Attributes

[33mcommit 3c4299707a8d56a758b862881a8545b032a00206[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Jan 27 11:16:04 2019 +0100

    Make TaskAttributeValue comparable

[33mcommit e6aefe54aaead2e9edaedd748c74e205d9da8fb0[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Jan 27 09:55:23 2019 +0100

    Add custom TaskList-Titles

[33mcommit 4cdbacac6675779f7a8113a0d9a9e474478fe8fb[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sat Jan 26 22:40:34 2019 +0100

    Changes to GroupBy-System

[33mcommit 41a42a869d1571c8edfa12b5718affdfdc875fec[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sat Jan 26 22:40:21 2019 +0100

    Changes TaskAttributeValue.toString()

[33mcommit 2eb08e7eb48fdd020b9f945428d7ff0712d37181[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sat Jan 26 21:22:51 2019 +0100

    Comments + Refactor

[33mcommit 99442f5c88c2198aedf9125ae6e0f232db2db6ee[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sat Jan 26 19:38:47 2019 +0100

    Changes to GroupBy-Logic

[33mcommit 28303fbf266e2be7a457a44e3c9af7c2cb58825a[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Fri Jan 25 15:18:39 2019 +0100

    Fixed Bug: Update Tasks when removing flag

[33mcommit 09445c1f3a2ce0feac383fd8a106a6cfda527757[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Jan 24 21:07:15 2019 +0100

    Display Tasks+Lists; Changes, Bug Fixes

[33mcommit 0c99e6b1b5b9f53e713655d7eb2de738ae921a1f[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Jan 24 20:12:56 2019 +0100

    Added *AttributeData.getDefault() / .usesDefault()

[33mcommit d7c20d82d473f6a8542ef42b88399f8923fed88c[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Tue Jan 22 11:09:44 2019 +0100

    Add toString() to TaskAttributeValues

[33mcommit 5d5d3d98902d83e4fdf9f159064d88db5775c140[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Tue Jan 22 10:55:43 2019 +0100

    Add hash(),equals() to TaskAttributeValues

[33mcommit 581abf7f6296f893546bc5d2688e1336c37e3a8a[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Tue Jan 22 09:30:23 2019 +0100

    Clean-up

[33mcommit f20ef9443b94e2a33cb33914a58f6ccec0ba59be[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Jan 17 15:31:29 2019 +0100

    Add Task, TaskLogic

[33mcommit 095de12ddde7bf613416fbacaae43a06a1580b1c[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Jan 17 15:07:10 2019 +0100

    Add wrapper for Attribute-Values

[33mcommit 48b6643e034c314e2c4eba6cd21567643498aa96[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Jan 17 00:03:35 2019 +0100

    Add info-badges to filter/group/sort-buttons

[33mcommit e4248e2e3559749328bcd554db520098d0663c00[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Wed Jan 16 22:44:30 2019 +0100

    Filter/GroupBy/Sort

[33mcommit e1c12bc179fb719e1469f30a7b16a8e14336c404[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Wed Jan 16 10:02:06 2019 +0100

    Improved TaskAttribute-update-system

[33mcommit 4a3153a56de8d09edd3cac74adf63f6226e33b51[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Tue Jan 15 23:40:59 2019 +0100

    Reworked LogicSystem + TaskAttribute-update-system

[33mcommit 87d8c021ab5936a264252973e7e2e595215ebbed[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Tue Jan 15 21:43:09 2019 +0100

    Added ui for filter/group/sort tasks + simple logic; Small changes

[33mcommit 8f1298468cedcb3c3353da8974c82427c1bc0ecf[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Fri Jan 11 15:26:10 2019 +0100

    Rework/Remove View-System

[33mcommit 8488dd2798f22cb5e0a4c6cc53a248c6d129207f[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Fri Jan 11 14:25:45 2019 +0100

    Changes

[33mcommit f9c4949f7f43e6bde9e49211d75a140ff8343901[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Jan 10 20:05:24 2019 +0100

    Drop logic_v1

[33mcommit 2c7b3542b3cda28dd1d303f83fd33a0f40d08a0e[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Thu Jan 10 16:08:36 2019 +0100

    Reworked TaskAttribute-System

[33mcommit 79bb3619eb3c6834d8c65f41f0edfa2ce611dcac[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Tue Jan 8 12:44:32 2019 +0100

    Changes

[33mcommit 11905c873326df6e1961cbf9534d69b28f9fb9d4[m
Author: Lukas Ruegner <ruegnerlukas@gmail.com>
Date:   Sun Dec 2 22:59:10 2018 +0100

    init
