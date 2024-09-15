(define range
  (lambda (start stop step)
    (let ((range-helper
            (lambda (current acc)
              (if (or (and (> step 0) (>= current stop))
                      (and (< step 0) (<= current stop)))
                  (reverse acc)
                  (range-helper (+ current step) (cons current acc))))))
      (range-helper start '()))))

(define reverse
  (lambda (list)
    (let ((reverse-helper
            (lambda (list acc)
              (if (null? list)
                  acc
                  (reverse-helper (cdr list) (cons (car list) acc))))))
      (reverse-helper list '()))))


(define filter
  (lambda (pred list)
    (let ((filter-helper
            (lambda (list acc)
              (if (null? list)
                  (reverse acc)
                  (if (pred (car list))
                      (filter-helper (cdr list) (cons (car list) acc))
                      (filter-helper (cdr list) acc))))))
      (filter-helper list '()))))






(define foldl
  (lambda (op init l)
    (let ((foldl-helper
            (lambda (acc l)
              (if (null? l)
                  acc
                  (foldl-helper (op acc (car l)) (cdr l))))))
      (foldl-helper init l))))






