;; for the purpose of this project, my mail address is @cscott.net
(setq user-mail-address "cscott@cscott.net")
;; useful file-munging stuff
(defun harpoon-subdir-name (basename-fragment)
  "Returns the subdirectory of the current buffer, after stripping everything up to and including the basename-fragment."
  (let ((directory (file-name-directory buffer-file-name)))
    (string-match (concat "\\(.*" basename-fragment "\\)*\\(.*\\)/$")
		  directory)
    (substring directory (match-end 1) (match-end 2))))
;;;; extract a dot-delimited package name from the path to the current buffer
(defun harpoon-package-name (basename-fragment)
  "Returns the package name of the current buffer, after stripping the directory up to and including the basename-fragment"
  (let ((pkgname (harpoon-subdir-name basename-fragment)))
    (while (string-match "\\(.*\\)/\\(.*\\)" pkgname)
      (setq pkgname (concat 
		     (substring pkgname (match-beginning 1) (match-end 1)) "."
		     (substring pkgname (match-beginning 2) (match-end 2)))))
    pkgname))
;;;; extract the work directory name from the buffer-file-name
(defun harpoon-basepath-name (basename-fragment)
  "Returns the subdirectory of the current buffer, after stripping everything after (but not including) the basename-fragment."
  (let ((directory (file-name-directory buffer-file-name)))
    (string-match (concat "\\(.*" basename-fragment "\\)*\\(.*\\)$")
		  directory)
    (substring directory (match-beginning 1) (match-end 1))))
;;;; extract the root project dir
(defun user-specific-project-dir ()
  "Returns the pathname to the root project directory."
  (harpoon-basepath-name "/GJDoc/"))
;; JDE stuff
(jde-project-file-version "1.0")
(jde-set-variables
'(jde-gen-class-buffer-template (quote 
    ("\"// \" (file-name-nondirectory buffer-file-name)"
     "\", created \" (current-time-string)" "\" by \" (user-login-name) 'n"
     "(funcall jde-gen-boilerplate-function)"
     "\"// Copyright (C) 2003 \" (user-full-name) \" <\" user-mail-address \">\" 'n" 
     "\"// Licensed under the terms of the GNU GPL; see COPYING for details.\" 'n" 
     "\"package net.cscott.gjdoc\" (harpoon-package-name \"/src\") \";\" 'n 'n" 
     "\"/**\" 'n" 
     "\" * The <code>\"" "(file-name-sans-extension (file-name-nondirectory buffer-file-name))" "\"</code> class...\" 'n" 
     "\" * \" 'n" 
     "\" * @author  \" (user-full-name)" "\" <\" user-mail-address \">\" 'n" 
     "\" * @version $I\" \"d$\" 'n" 
     "\" */\" 'n>" 
     "\"public class \"" 
     "(file-name-sans-extension (file-name-nondirectory buffer-file-name))" 
     "(jde-gen-get-extend-class) \" {\" 'n> 'n>"
     "\"/** Creates a <code>\" " 
     "(file-name-sans-extension (file-name-nondirectory buffer-file-name))" 
     "\"</code>. */\" 'n" 
     "\"public \"" 
     "(file-name-sans-extension (file-name-nondirectory buffer-file-name))" 
     "\"() {\" 'n>" 
     "\"    \" 'p 'n>" 
     "\"}\" 'n>" 
     "'n>" "\"}\"")) t)
)
