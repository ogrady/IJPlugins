# IJPlugins
## Preface
The ImageJ-plugins in this repository are created as exercises for the lecture "Anwendung der Multimediatechnik" at the University of Tübingen.
They are therefore not necessarily fast, elegant or even correct and created in accordance with the exercises of said lecture.
So they might not fit your purpose, but you are welcome to see them as examples for the used filters and techniques and to fork this repository for your own applications.

## Prerequisites
At the time of writing this guide, I've been using 

- ImageJ 1.48
- Java 1.8.0_45
- Eclipse Kepler Release (Build id: 20130614-0229)
 
## Setup
This is merely a recommended quick-start guide I found useful for working with ImageJ and eclipse. There might be better solutions. 

(0. download ImageJ from http://imagej.nih.gov/ij/ and install it on your computer)

1. fire up eclipse and set up a worksspace that points to `imagej_installation_directory/plugins`
2. make sure you have opened your git-view from `Window -> Show View -> Other... -> Git -> Git Repositories`. If you don't have it installed, you can download the eGit-plugin from http://www.eclipse.org/egit/
3. in the git-view, clone this repository by entering the URL to the github-repository and following the wizard. When eclipse asks you for the Local Destination, choose `imagej_installation_directory/plugins/OgrPlugins`
4. right-click the new repository in the git-view and select `Import Projects...` and import the existing project as suggested
5. now edit the project-properties (`right-click -> Properties`).
5.1 under "Libraries" add the ij.jar that should be contained within your ImageJ installation-directory vid "Add External JARs..."
6. when opening ImageJ and selecting "Plugins" you should now have a new option "OgrPlugins". This is, of course, the name of the project as entered in step 3 and can be anything you want.
